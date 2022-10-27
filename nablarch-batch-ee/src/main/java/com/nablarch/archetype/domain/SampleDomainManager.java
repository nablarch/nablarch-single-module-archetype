package com.nablarch.archetype.domain;

import nablarch.core.validation.ee.DomainManager;

/**
 *  {@link DomainManager}の実装クラス。
 * ドメインを定義したBeanクラスを返却する。
 */
public class SampleDomainManager implements DomainManager<SampleDomainBean> {

    @Override
    public Class<SampleDomainBean> getDomainBean() {
        // ドメインBeanのClassオブジェクトを返す
        return SampleDomainBean.class;
    }
}
