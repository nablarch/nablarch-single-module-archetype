package com.nablarch.archetype;

import org.junit.Test;

import com.nablarch.archetype.test.BatchRequestTestBase;

/**
 * {@link SampleBatch}のテストクラス。
 *
 * @deprecated TODO 疎通確認完了後、削除して下さい。
 */
public class SampleBatchActionRequestTest extends BatchRequestTestBase {


    /** 正常終了のテストケース。 */
    @Test
    public void testNormalEnd() {
        execute();
    }

}
