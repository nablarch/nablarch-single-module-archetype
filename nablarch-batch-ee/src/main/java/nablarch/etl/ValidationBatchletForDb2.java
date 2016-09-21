package nablarch.etl;

import nablarch.common.dao.DeferredEntityList;
import nablarch.common.dao.EntityUtil;
import nablarch.common.dao.UniversalDao;
import nablarch.core.beans.BeanUtil;
import nablarch.core.db.connection.AppDbConnection;
import nablarch.core.db.connection.DbConnectionContext;
import nablarch.core.db.statement.SqlPStatement;
import nablarch.core.log.Logger;
import nablarch.core.log.LoggerManager;
import nablarch.core.transaction.TransactionContext;
import nablarch.core.util.StringUtil;
import nablarch.core.validation.ee.ValidatorUtil;
import nablarch.etl.config.EtlConfig;
import nablarch.etl.config.RootConfig;
import nablarch.etl.config.ValidationStepConfig;
import nablarch.etl.config.ValidationStepConfig.Mode;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Table;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.text.MessageFormat;
import java.util.Set;

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
    @Inject
    private JobContext jobContext;

    /** {@link StepContext} */
    @Inject
    private StepContext stepContext;

    /** ETLの設定 */
    @Inject
    @EtlConfig
    private RootConfig rootConfig;

    @Override
    public String process() throws Exception {

        final ValidationStepConfig stepConfig = rootConfig.getStepConfig(
                jobContext.getJobName(), stepContext.getStepName());

        verifyConfig(stepConfig);

        final Class<?> inputTable = stepConfig.getBean();
        final Class<?> errorTable = stepConfig.getErrorEntity();

        truncateErrorTable(errorTable);

        final ValidationResult validationResult = new ValidationResult();
        final Validator validator = ValidatorUtil.getValidator();

        // 一時テーブルのデータを全て取得しValidationを行う。
        final DeferredEntityList<?> workItems = (DeferredEntityList<?>) UniversalDao.defer().findAll(inputTable);

        for (Object item : workItems) {
            validationResult.incrementCount();

            final WorkItem workItem = (WorkItem) item;
            final Set<ConstraintViolation<WorkItem>> constraintViolations = validator.validate(workItem);

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
        workItems.close();

        deleteErrorRecord(inputTable, errorTable);

        LOGGER.logInfo(MessageFormat.format(
                "validation result. bean class=[{0}], line count=[{1}], error count=[{2}]",
                inputTable.getName(), validationResult.getLineCount(), validationResult.getErrorCount()));

        // トランザクションをコミットし処理を終了する。
        // トランザクションをコミットしない場合、ジョブを異常終了するモードの場合に、
        // エラーテーブルに格納した情報などが破棄されてしまう。
        commit();

        return buildResult(stepConfig.getMode(), inputTable, validationResult);
    }

    /**
     * エラーテーブルの内容をクリーニングする。
     * etl.jsonでerrorTableに指定されたrンティティクラスに{@link javax.persistence.Table}が無い場合、
     * もしくはテーブル名の指定が無い場合は処理を行わない。
     *
     * @param errorTable エラーテーブルのエンティティクラス
     */
    private static void truncateErrorTable(final Class<?> errorTable) {
        final AppDbConnection connection = DbConnectionContext.getConnection();
        String tableNameWithSchema = getTableNameWithSchema(errorTable);
        if (StringUtil.hasValue(tableNameWithSchema)) {
            final SqlPStatement statement = connection.prepareStatement(
                    "truncate table " + tableNameWithSchema + " immediate" );
            statement.execute();
            commit();
        }
    }

    /**
     * エンティティクラスからテーブル名を取得する。
     * スキーマの指定がある場合、スキーマ名.テーブル名の形式で
     * 取得する。
     * <p/>
     * 引数のクラスに{@link javax.persistence.Table}が無い場合は
     * 空文字列を返す。
     *
     * @param entity テーブル名を取得するエンティティクラス。
     * @return テーブル名。
     */
    private static String getTableNameWithSchema(Class<?> entity){
        final Table table = entity.getAnnotation(Table.class);
        if (table == null){
            return "";
        }
        if (StringUtil.isNullOrEmpty(table.schema())) {
            return table.name();
        } else {
            return table.schema() + '.' + table.name();
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
     * @param stepConfig 設定値
     */
    private void verifyConfig(final ValidationStepConfig stepConfig) {
        final String jobName = jobContext.getJobName();
        final String stepName = stepContext.getStepName();

        EtlUtil.verifyRequired(jobName, stepName, "bean", stepConfig.getBean());
        EtlUtil.verifyRequired(jobName, stepName, "errorEntity", stepConfig.getErrorEntity());
        EtlUtil.verifyRequired(jobName, stepName, "mode", stepConfig.getMode());
    }

    /**
     * 結果を構築する。
     *
     * @param mode モード
     * @param inputTableEntity 入力Entityクラス
     * @param validationResult バリデーション結果
     * @return 終了ステータス
     */
    private static String buildResult(
            final Mode mode, final Class<?> inputTableEntity, final ValidationResult validationResult) {

        if (validationResult.hasError()) {
            if (mode == Mode.CONTINUE) {
                return "VALIDATION_ERROR";
            } else {
                throw new EtlJobAbortedException(
                        "abort the JOB because there was a validation error."
                                + " bean class=[" + inputTableEntity.getName() + "],"
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
            ));
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

}
