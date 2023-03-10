package com.nablarch.archetype.chunk;

import com.nablarch.archetype.entity.SampleUser;

import java.io.Serializable;
import java.util.Iterator;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import nablarch.common.dao.DeferredEntityList;
import nablarch.common.dao.UniversalDao;
import nablarch.fw.batch.ee.chunk.BaseDatabaseItemReader;
import nablarch.fw.batch.ee.progress.ProgressManager;

/**
 * 疎通確認用ItemReader実装クラス。
 * @deprecated TODO 疎通確認が終わったら削除すること。
 */
@Dependent
@Named
public class SampleReader extends BaseDatabaseItemReader {

    /**
     * データベースから取得したデータのリスト。
     */
    private DeferredEntityList<SampleUser> list;

    /**
     * イテレータ。
     */
    private Iterator<SampleUser> iterator;

    /**
     * 進捗管理Bean。
     */
    private final ProgressManager progressManager;

    /**
     * {@link ProgressManager}をインジェクトするコンストラクタ。
     * @param progressManager 進捗管理Bean
     */
    @Inject
    public SampleReader(ProgressManager progressManager) {
        this.progressManager = progressManager;
    }

    @Override
    protected void doOpen(final Serializable checkpoint) throws Exception {
        progressManager.setInputCount(UniversalDao.countBySqlFile(SampleUser.class, "SELECT_SAMPLE_USER"));

        list = (DeferredEntityList<SampleUser>) UniversalDao.defer()
                                                            .findAll(SampleUser.class);

        iterator = list.iterator();
    }

    /**
     * 取得したデータのリストから一件づつ取り出す。
     * @return データ
     */
    @Override
    public Object readItem() {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    /**
     * すべてのデータの読み込みが終了したのち、
     * {@link DeferredEntityList#close}を実行する。
     * @throws Exception {@inheritDoc}
     */
    @Override
    public void doClose() throws Exception {
        if (list != null) {
            list.close();
        }
    }

}
