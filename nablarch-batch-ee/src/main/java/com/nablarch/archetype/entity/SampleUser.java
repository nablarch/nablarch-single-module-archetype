package com.nablarch.archetype.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * 疎通確認用のEntityクラス。
 *
 * @deprecated TODO 疎通確認が終わったら削除すること。
 */
@Entity
@Table(name = "SAMPLE_USER")
public class SampleUser implements Serializable {

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
