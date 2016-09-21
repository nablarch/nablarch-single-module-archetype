package com.nablarch.archetype.domain;

import nablarch.core.validation.ee.Length;
import nablarch.core.validation.ee.SystemChar;

/**
 * ドメイン定義の例。
 */
public class SampleDomainBean {

    /**
     * USER_IDのドメイン定義。
     */
    @SystemChar(charsetDef = "半角数字")
    @Length(min = 1, max = 20)
    public String userId;

    /**
     * 姓のドメイン定義。
     */
    @SystemChar(charsetDef = "全角文字")
    @Length(max = 20)
    public String familyName;

    /**
     * 名のドメイン定義。
     */
    @SystemChar(charsetDef = "全角文字")
    @Length(max = 20)
    public String firstName;

}
