package com.nablarch.archetype.form;

import nablarch.common.databind.csv.Csv;

/**
 * 編集したSAMPLE_USERテーブルのデータをファイルに出力する際に使用する
 * formクラス。
 *
 * @deprecated TODO 疎通確認が終わったら削除すること。
 */
@Csv(
        type = Csv.CsvType.DEFAULT,
        properties = {"userId", "name" },
        headers = {"ユーザID", "氏名" })
public class SampleUserForm {

    /** ユーザID。 */
    private String userId;

    /** 氏名。 */
    private String name;

    /**
     * ユーザIDを取得する。
     * @return ユーザID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * ユーザIDを設定する。
     *
     * @param userId ユーザID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 氏名を取得する。
     *
     * @return 氏名
     */
    public String getName() {
        return name;
    }

    /**
     * 氏名を設定する。
     *
     * @param name 氏名
     */
    public void setName(String name) {
        this.name = name;
    }
}
