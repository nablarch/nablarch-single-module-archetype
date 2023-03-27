package com.nablarch.archetype;

import nablarch.common.databind.ObjectMapper;
import nablarch.common.databind.ObjectMapperFactory;
import nablarch.core.log.Logger;
import nablarch.core.log.LoggerManager;
import nablarch.core.message.MessageUtil;
import nablarch.core.util.FilePathSetting;
import nablarch.fw.ExecutionContext;
import nablarch.fw.Result;
import nablarch.fw.Result.Success;
import nablarch.fw.action.NoInputDataBatchAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 疎通確認用の都度起動バッチアクションクラス。
 * <p>
 * 以下の機能について、疎通確認を行う。
 * </p>
 * <ul>
 * <li>ディスパッチ機能</li>
 * <li>メッセージ機能</li>
 * </ul>
 * <p>
 * 疎通確認に失敗した場合は、その時点で例外が発生する。
 * </p>
 * <p>
 * 全ての疎通確認に成功した場合、データバインドを使用して、
 * 各機能の疎通結果がファイル出力される(test-result.csv)。
 * </p>
 * <pre>
 * 【出力例】
 * "dispatch", "OK"
 * "message","OK"
 * </pre>
 *
 * @deprecated TODO 疎通確認完了後、削除して下さい。
 */
public class SampleBatch extends NoInputDataBatchAction {

    /**
     * ロガー。
     */
    private static final Logger LOGGER = LoggerManager.get(SampleBatch.class);

    /**
     * 出力ファイルのベースパス。
     */
    private static final String OUTPUT_BASE_PATH_NAME = "output";

    /**
     * 出力ファイル。
     */
    private static final String OUTPUT_FILE_NAME = "test-result.csv";

    /**
     * 疎通確認用に使用するメッセージID。
     */
    private static final String MESSAGE_ID = "sample.error.message";

    /**
     * {@inheritDoc}
     */
    @Override
    public Result handle(ExecutionContext ctx) {

        LOGGER.logInfo("疎通確認を開始します。");

        try (ObjectMapper<SampleBatchOutput> mapper = createOutputObjectMapper()) {
            // ディスパッチ機能の疎通確認
            // (このメソッドが呼ばれたということはディスパッチ機能は機能している)
            mapper.write(createOkRecord("dispatch"));

            // メッセージ機能の疎通確認
            checkMessageFunction();
            mapper.write(createOkRecord("message"));
        }

        LOGGER.logInfo("疎通確認が完了しました。");

        return new Success("疎通確認が完了しました。");
    }

    /**
     * ファイルを出力するための{@link ObjectMapper}を生成します。
     *
     * @return ファイルを出力するための {@code ObjectMapper}
     */
    private ObjectMapper<SampleBatchOutput> createOutputObjectMapper() {
        File file = FilePathSetting.getInstance().getFile(OUTPUT_BASE_PATH_NAME, OUTPUT_FILE_NAME);
        try {
            return ObjectMapperFactory.create(SampleBatchOutput.class, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(
                    "出力ファイルの作成に失敗しました。ネストした例外メッセージを確認して下さい。", e);
        }
    }

    /**
     * メッセージ機能の疎通確認を行う。
     */
    private void checkMessageFunction() {
        try {
            MessageUtil.getStringResource(MESSAGE_ID);
        } catch (RuntimeException e) {
            throw new IllegalStateException(
                    "メッセージ機能の疎通確認に失敗しました。ネストした例外メッセージを確認して下さい。", e);
        }
    }

    /**
     * 出力用のデータオブジェクトを作成する。
     *
     * @param okFunctionName 疎通確認OKであった機能名
     */
    private SampleBatchOutput createOkRecord(String okFunctionName) {
        SampleBatchOutput outputData = new SampleBatchOutput();
        outputData.setFunctionName(okFunctionName);
        outputData.setResult("OK");
        return outputData;
    }
}
