package com.nablarch.archetype.chunk;

import com.nablarch.archetype.entity.SampleUser;
import nablarch.common.dao.DeferredEntityList;
import nablarch.common.dao.UniversalDao;
import nablarch.fw.batch.ee.progress.ProgressManager;

import javax.batch.api.chunk.AbstractItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Iterator;

/**
 * 疎通確認用ItemReader実装クラス。
 * @deprecated TODO 疎通確認が終わったら削除すること。
 */
@Dependent
@Named
public class SampleReader extends AbstractItemReader {

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

    /**
     * データベースからサンプルユーザデータのリストを取得する。
     * @param checkpoint {@inheritDoc}
     * @throws Exception {@inheritDoc}
     */
    @Override
    public void open(Serializable checkpoint) throws Exception {
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
    public void close() throws Exception {
        if (list != null) {
            list.close();
        }
    }
}
