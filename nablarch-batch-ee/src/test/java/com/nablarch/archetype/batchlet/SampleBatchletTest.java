package com.nablarch.archetype.batchlet;

import com.nablarch.archetype.chunk.SampleProcessor;
import com.nablarch.archetype.entity.SampleUser;
import nablarch.common.dao.UniversalDao;
import nablarch.test.core.db.DbAccessTestSupport;
import nablarch.test.junit5.extension.db.DbAccessTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link SampleProcessor}のテストクラス
 * 疎通確認用に作成しています。
 * <p>
 * @deprecated TODO 疎通確認が終わったら削除してください。
 */
@DbAccessTest
class SampleBatchletTest {
    DbAccessTestSupport support;

    /**
     * {@link SampleBatchlet#process()} のテスト
     */
    @Test
    void testProcess() {
        SampleBatchlet target = new SampleBatchlet();
        target.process();

        //SAMPLE_USERテーブルの全件検索を行い、一件もデータが存在しないことを確認する。
        int actual = UniversalDao.findAll(SampleUser.class).size();
        assertEquals(0, actual);
    }
}
