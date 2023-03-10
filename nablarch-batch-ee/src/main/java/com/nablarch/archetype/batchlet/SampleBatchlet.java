package com.nablarch.archetype.batchlet;

import com.nablarch.archetype.entity.SampleUser;
import nablarch.common.dao.EntityList;
import nablarch.common.dao.UniversalDao;
import nablarch.core.log.Logger;
import nablarch.core.log.LoggerManager;

import jakarta.batch.api.AbstractBatchlet;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;

/**
 * 疎通確認用バッチレット。
 *
 * @deprecated TODO 疎通確認が終わったら削除すること。
 */
@Dependent
@Named
public class SampleBatchlet extends AbstractBatchlet {

    /** ロガー。 */
    private static final Logger LOGGER =
            LoggerManager.get(SampleBatchlet.class);

    @Override
    public String process() {

        /*
            このクラスは疎通確認用のクラスであり、UniversalDaoの動作確認のため
            下記のような実装をしている。
         */
        int deleteCount = 0;
        EntityList<SampleUser> userDataList =
                UniversalDao.findAll(SampleUser.class);
        for (SampleUser sampleUser : userDataList) {
            deleteCount += UniversalDao.delete(sampleUser);
        }
        LOGGER.logInfo("削除件数：" + deleteCount + "件");
        return "SUCCESS";
    }
}
