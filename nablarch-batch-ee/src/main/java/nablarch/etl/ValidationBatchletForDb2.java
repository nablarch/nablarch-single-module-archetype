package nablarch.etl;

import static nablarch.etl.EtlUtil.verifyRequired;

import java.text.MessageFormat;
import java.util.Set;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import nablarch.common.dao.DeferredEntityList;
import nablarch.common.dao.EntityUtil;
import nablarch.common.dao.UniversalDao;
import nablarch.core.beans.BeanUtil;
import nablarch.core.db.connection.AppDbConnection;
import nablarch.core.db.connection.DbConnectionContext;
import nablarch.core.db.statement.ResultSetIterator;
import nablarch.core.db.statement.SqlPStatement;
import nablarch.core.log.Logger;
import nablarch.core.log.LoggerManager;
import nablarch.core.log.basic.LogLevel;
import nablarch.core.log.operation.OperationLogger;
import nablarch.core.message.MessageLevel;
import nablarch.core.message.MessageUtil;
import nablarch.core.transaction.TransactionContext;
import nablarch.core.validation.ee.ValidatorUtil;
import nablarch.etl.config.EtlConfig;
import nablarch.etl.config.StepConfig;
import nablarch.etl.config.ValidationStepConfig;
import nablarch.etl.config.ValidationStepConfig.Mode;
import nablarch.fw.batch.ee.progress.ProgressManager;

/**
 * 一時テーブルのデータをバリデーションする{@link nablarch.etl.ValidationBatchlet}のDB2対応版。
 * <p/>
 * 機能の詳細は{@link nablarch.etl.ValidationBatchlet}を参照すること。
 */
@Named
@Dependent
public class ValidationBatchletForDb2 extends AbstractBatchlet {

    /** ロガー */
    private static final Logger LOGGER = LoggerManager.get("etl");

    /** {@link JobContext} */
    private final JobContext jobContext;

    /** {@link StepContext} */
    private final StepContext stepContext;

    /** ETLの設定 */
    private final ValidationStepConfig stepConfig;

    /** 進捗状況を管理するBean */
    private final ProgressManager progressManager;

    /** 進捗ログを出す間隔 */
    @Inject
    @BatchProperty
    String progressLogOutputInterval = "1000";

    /**
     * コンストラクタ。
     * @param jobContext {@link JobContext}
     * @param stepContext {@link StepContext}
     * @param stepConfig ステップの設定
     * @param progressManager 進捗状況を管理するBean
     */
    @Inject
    public ValidationBatchletForDb2(
            final JobContext jobContext,
            final StepContext stepContext,
            @EtlConfig final StepConfig stepConfig,
            final ProgressManager progressManager) {
        this.jobContext = jobContext;
        this.stepContext = stepContext;
        this.stepConfig = (ValidationStepConfig) stepConfig;
        this.progressManager = progressManager;
    }

    @Override
    public String process() throws Exception {

        verifyConfig();

        final Class<?> inputTable = stepConfig.getBean();
        final Class<?> errorTable = stepConfig.getErrorEntity();

        truncateErrorTable(errorTable);

        final ValidationResult validationResult = new ValidationResult();
        final Validator validator = ValidatorUtil.getValidator();

        final long logInterval = getLogInterval();

        // 一時テーブルのデータを全て取得しValidationを行う。
        progressManager.setInputCount(getRecordCountInInputTable());
        final DeferredEntityList<?> workItems = (DeferredEntityList<?>) UniversalDao.defer().findAll(inputTable);

        for (Object item : workItems) {
            validationResult.incrementCount();

            final WorkItem workItem = (WorkItem) item;
            final Set<ConstraintViolation<WorkItem>> constraintViolations = validator.validate(workItem);

            if (validationResult.getLineCount() % logInterval == 0L) {
                progressManager.outputProgressInfo(validationResult.getLineCount());
            }

            if (constraintViolations.isEmpty()) {
                continue;
            }

            validationResult.addErrorCount(constraintViolations.size());
            onError(workItem, constraintViolations, errorTable);
            if (isOverLimit(stepConfig, validationResult)) {
                throw new EtlJobAbortedException("number of validation errors has exceeded the maximum number of errors."
                        + " bean class=[" + inputTable.getName() + ']');
            }
        }

        if (validationResult.getLineCount() % logInterval != 0L) {
            progressManager.outputProgressInfo(validationResult.getLineCount());
        }
        workItems.close();

        deleteErrorRecord(inputTable, errorTable);

        LOGGER.logInfo(MessageFormat.format(
                "validation result. bean class=[{0}], line count=[{1}], error count=[{2}]",
                inputTable.getName(), validationResult.getLineCount(), validationResult.getErrorCount()));

        // トランザクションをコミットし処理を終了する。
        // トランザクションをコミットしない場合、ジョブを異常終了するモードの場合に、
        // エラーテーブルに格納した情報などが破棄されてしまう。
        commit();

        return buildResult(validationResult);
    }

    /**
     * エラーテーブルの内容をクリーニングする。
     *
     * @param errorTable エラーテーブルの内容
     */
    private static void truncateErrorTable(final Class<?> errorTable) {
        final AppDbConnection connection = DbConnectionContext.getConnection();
        final SqlPStatement statement = connection.prepareStatement(
                "truncate table " + EntityUtil.getTableNameWithSchema(errorTable) + " immediate");
        statement.executeUpdate();
        commit();
    }

    /**
     * 入力テーブルのレコード数を取得する。
     * @return レコード数
     */
    private long getRecordCountInInputTable() {
        final AppDbConnection connection = DbConnectionContext.getConnection();
        final SqlPStatement statement = connection.prepareStatement(
                "select count(*) from " + EntityUtil.getTableNameWithSchema(stepConfig.getBean()));
        try {
            final ResultSetIterator rows = statement.executeQuery();
            rows.next();
            return rows.getLong(1);
        } finally {
            statement.close();
        }
    }

    /**
     * 一時テーブルからエラーのレコード情報を削除する。
     *
     * @param inputTable 一時テーブル
     * @param errorTable エラーテーブル
     */
    private static void deleteErrorRecord(final Class<?> inputTable, final Class<?> errorTable) {
        final AppDbConnection connection = DbConnectionContext.getConnection();
        final SqlPStatement statement = connection.prepareStatement(buildDeleteSql(inputTable, errorTable));
        statement.executeUpdate();
    }

    /**
     * エラーレコードをクリーニングするためのSQL文を構築する。
     *
     * @param inputTable 一時テーブル
     * @param errorTable エラーテーブル
     * @return SQL文
     */
    private static String buildDeleteSql(final Class<?> inputTable, final Class<?> errorTable) {
        return "delete from " + EntityUtil.getTableNameWithSchema(inputTable)
                + " where line_number in (select line_number from " + EntityUtil.getTableNameWithSchema(errorTable) + ')';
    }

    /**
     * 設定値の検証を行う。
     *
     */
    private void verifyConfig() {
        final String jobName = jobContext.getJobName();
        final String stepName = stepContext.getStepName();

        verifyRequired(jobName, stepName, "bean", stepConfig.getBean());
        verifyRequired(jobName, stepName, "errorEntity", stepConfig.getErrorEntity());
        verifyRequired(jobName, stepName, "mode", stepConfig.getMode());
    }

    /**
     * 結果を構築する。
     *
     * @param validationResult バリデーション結果
     * @return 終了ステータス
     */
    private String buildResult(final ValidationResult validationResult) {

        if (validationResult.hasError()) {
            OperationLogger.write(LogLevel.ERROR,
                    MessageUtil.createMessage(MessageLevel.ERROR, "nablarch.etl.validation-error").formatMessage());
            if (stepConfig.getMode() == Mode.CONTINUE) {
                jobContext.setExitStatus("WARNING");
                return "WARNING";
            } else {
                throw new EtlJobAbortedException(
                        "abort the JOB because there was a validation error."
                                + " bean class=[" + stepConfig.getBean().getName() + "],"
                                + " error count=[" + validationResult.getErrorCount() + ']');
            }
        } else {
            return "SUCCESS";
        }
    }


    /**
     * Validationエラー時の処理を行う。
     *
     * @param item Validationエラーが発生したアイテム
     * @param constraintViolations Validationのエラー内容
     * @param errorTable エラーテーブルのエンティティクラス
     */
    private static void onError(
            final WorkItem item,
            final Set<ConstraintViolation<WorkItem>> constraintViolations,
            final Class<?> errorTable) {

        for (ConstraintViolation<WorkItem> violation : constraintViolations) {
            LOGGER.logWarn(MessageFormat.format(
                    "validation error has occurred. bean class=[{0}], property name=[{1}], error message=[{2}], line number=[{3}]",
                    item.getClass()
                            .getName(),
                    violation.getPropertyPath()
                            .toString(),
                    violation.getMessage(),
                    item.getLineNumber()
                    )
            );

        }

        UniversalDao.insert(BeanUtil.createAndCopy(errorTable, item));
    }

    /**
     * エラーの許容数を超えたか否か。
     *
     * @param stepConfig 設定値
     * @param validationResult バリデーション結果情報
     * @return 超えている場合は{@code true}
     */
    private static boolean isOverLimit(final ValidationStepConfig stepConfig, final ValidationResult validationResult) {
        final Integer errorLimitCount = stepConfig.getErrorLimit();
        if (errorLimitCount == null || errorLimitCount.compareTo(0) < 0) {
            return false;
        }
        return validationResult.getErrorCount() > errorLimitCount;
    }

    /**
     * コミットを行う。
     */
    private static void commit() {
        TransactionContext.getTransaction().commit();
    }

    /**
     * 進捗ログの出力間隔を取得する。
     * @return 進捗ログの出力間隔
     */
    private long getLogInterval() {
        try {
            return Long.parseLong(progressLogOutputInterval);
        } catch (NumberFormatException e) {
            LOGGER.logWarn("progress log output interval is not numeric. use the default value(1000)");
            return 1000L;
        }
    }
}
