package com.nablarch.archetype.dto;

import nablarch.common.databind.csv.Csv;
import nablarch.core.validation.ee.Domain;
import nablarch.etl.WorkItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CSVからデータを読み込み一時テーブルに
 * 投入する際に使用するDtoクラス。
 * ETL機能で使用する。
 *
 * @deprecated TODO 疎通確認が終わったら削除すること。
 */
@Entity
@Table(name = "SAMPLE_USER_WORK")
@Csv(
        type = Csv.CsvType.DEFAULT,
        properties = {
                "userId",
                "familyName",
                "firstName" },
        headers = {
                "ユーザID",
                "姓",
                "名" })
public class SampleUserDto extends WorkItem {

    /** ユーザID。 */
    private String userId;

    /** 姓。 */
    private String familyName;

    /** 名。 */
    private String firstName;

    /**
     * ユーザIDを返します。
     *
     * @return ユーザID
     */
    @Id
    @Column(name = "USER_ID",
            length = 20,
            nullable = false,
            unique = true)
    @Domain("userId")
    public String getUserId() {
        return userId;
    }

    /**
     * ユーザIDを設定します。
     *
     * @param userId ユーザID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * 姓を返します。
     *
     * @return 姓
     */
    @Column(name = "FAMILY_NAME",
            length = 20,
            nullable = false,
            unique = false)
    @Domain("familyName")
    public String getFamilyName() {
        return familyName;
    }

    /**
     * 姓を設定します。
     *
     * @param familyName 姓
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    /**
     * 名を返します。
     *
     * @return 名
     */
    @Column(name = "FIRST_NAME",
            length = 20,
            nullable = false,
            unique = false)
    @Domain("firstName")
    public String getFirstName() {
        return firstName;
    }

    /**
     * 名を設定します。
     *
     * @param firstName 名
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
