package com.nablarch.archetype;

import com.nablarch.archetype.test.XxxxBatchRequestExtension;

import com.nablarch.archetype.test.XxxxBatchRequestTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * {@link SampleBatch}のテストクラス。
 *
 * @deprecated TODO 疎通確認完了後、削除して下さい。
 */
@ExtendWith(XxxxBatchRequestExtension.class)
class SampleBatchActionRequestTest {
    XxxxBatchRequestTestSupport support;

    /** 正常終了のテストケース。 */
    @Test
    void testNormalEnd() {
        support.execute(support.testName.getMethodName());
    }

}
