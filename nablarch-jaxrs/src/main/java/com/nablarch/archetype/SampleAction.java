package com.nablarch.archetype;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nablarch.archetype.dto.SampleUserListDto;
import com.nablarch.archetype.entity.SampleUser;

import nablarch.common.dao.EntityList;
import nablarch.common.dao.UniversalDao;
import nablarch.fw.web.HttpRequest;

/**
 * 疎通確認用のアクションクラス。
 *
 * @deprecated TODO 疎通確認用のクラスです。確認完了後、削除してください。
 */
public class SampleAction {

    /**
     * 検索処理。
     * <p>
     * 応答にJSONを使用する。
     * </p>
     * @param req HTTPリクエスト
     * @return ユーザ情報(JSON)
     */
    @Produces(MediaType.APPLICATION_JSON)
    public EntityList<SampleUser> findProducesJson(HttpRequest req) {
        return UniversalDao.findAll(SampleUser.class);
    }

    /**
     * 検索処理。
     * <p>
     * 応答にXMLを使用する。
     * </p>
     * @param req HTTPリクエスト
     * @return ユーザ情報(XML)
     */
    @Produces(MediaType.APPLICATION_XML)
    public SampleUserListDto findProducesXml(HttpRequest req) {
        EntityList<SampleUser> sampleUserList = UniversalDao.findAll(SampleUser.class);
        return new SampleUserListDto(sampleUserList);
    }

}
