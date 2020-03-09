package com.nablarch.archetype;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.nablarch.archetype.dto.SampleUserListDto;
import com.nablarch.archetype.entity.SampleUser;

import nablarch.common.dao.EntityList;
import nablarch.fw.web.MockHttpRequest;
import nablarch.test.core.db.DbAccessTestSupport;

/**
 * {@link SampleAction}のテストクラス。
 *
 * @deprecated TODO 疎通確認用のクラスです。確認完了後、削除してください。
 */
public class SampleActionTest extends DbAccessTestSupport {

    /**
     * 正常終了のテストケース。
     * <p>
     * DBからのデータ取得。
     * </p>
     */
    @Test
    public void testFind() {
        //テスト対象インスタンス化
        SampleAction target = new SampleAction();

        //テスト対象メソッド実行
        EntityList<SampleUser> sampleUsers = target.findProducesJson(new MockHttpRequest());

        //アサートの結果を一定にするためにソートする
        sampleUsers.sort((a, b) -> (a.getUserId() - b.getUserId()));

        //テスト対象メソッドの戻り値に対するアサート
        assertThat(sampleUsers.size(), is(2));
        assertThat(sampleUsers.get(0).getKanaName(), is("なぶらくたろう"));
        assertThat(sampleUsers.get(0).getKanjiName(), is("名部楽太郎"));
        assertThat(sampleUsers.get(1).getKanaName(), is("なぶらくじろう"));
        assertThat(sampleUsers.get(1).getKanjiName(), is("名部楽次郎"));
    }

    /**
     * 正常終了のテストケース。
     * <p>
     * DBからのデータ取得。
     * </p>
     */
    @Test
    public void testFindUsingXml() {
        //テスト対象インスタンス化
        SampleAction target = new SampleAction();

        //テスト対象メソッド実行
        SampleUserListDto findUsingXml = target.findProducesXml(new MockHttpRequest());
        List<SampleUser> sampleUserList = findUsingXml.getSampleUserList();

        //アサートの結果を一定にするためにソートする
        sampleUserList.sort((a, b) -> (a.getUserId() - b.getUserId()));

        //テスト対象メソッドの戻り値に対するアサート
        assertThat(sampleUserList.size(), is(2));
        assertThat(sampleUserList.get(0).getKanaName(), is("なぶらくたろう"));
        assertThat(sampleUserList.get(0).getKanjiName(), is("名部楽太郎"));
        assertThat(sampleUserList.get(1).getKanaName(), is("なぶらくじろう"));
        assertThat(sampleUserList.get(1).getKanjiName(), is("名部楽次郎"));
    }

}
