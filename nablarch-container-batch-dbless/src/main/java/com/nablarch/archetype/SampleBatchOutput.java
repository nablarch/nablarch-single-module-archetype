package com.nablarch.archetype;

import nablarch.common.databind.csv.Csv;
import nablarch.common.databind.csv.CsvDataBindConfig;
import nablarch.common.databind.csv.CsvFormat;

/**
 * {@link SampleBatchOutput}の結果を出力するためのデータオブジェクト。
 * 
 * @deprecated TODO 疎通確認完了後、削除して下さい。
 */
@Csv(type = Csv.CsvType.CUSTOM, properties = {"functionName", "result"})
@CsvFormat(
        fieldSeparator = ',',
        lineSeparator = "\r\n",
        quote = '"',
        ignoreEmptyLine = false,
        requiredHeader = false,
        charset = "UTF-8",
        quoteMode = CsvDataBindConfig.QuoteMode.ALL,
        emptyToNull = true)
public class SampleBatchOutput {

    /**
     * 疎通確認OKの機能名
     */
    private String functionName;

    /**
     * 判定結果
     */
    private String result;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
