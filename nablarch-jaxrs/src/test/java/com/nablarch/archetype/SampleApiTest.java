package com.nablarch.archetype;

import nablarch.fw.web.HttpResponse;
import nablarch.test.core.http.RestTestSupport;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.xmlunit.builder.Input;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

/**
 * {@link SampleAction}のテストクラス。
 *
 * @deprecated TODO 疎通確認用のクラスです。確認完了後、削除してください。
 */
public class SampleApiTest extends RestTestSupport {
    private static final String JSON_RESPONSE = "["
            + "{\"userId\": 1,\"kanjiName\": \"名部楽太郎\",\"kanaName\": \"なぶらくたろう\"},"
            + "{\"userId\": 2,\"kanjiName\": \"名部楽次郎\",\"kanaName\": \"なぶらくじろう\"}"
            + "]";
    private static final String XML_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
            + "<userList>\n"
            + "    <sampleUser>\n"
            + "        <kanaName>なぶらくたろう</kanaName>\n"
            + "        <kanjiName>名部楽太郎</kanjiName>\n"
            + "        <userId>1</userId>\n"
            + "    </sampleUser>\n"
            + "    <sampleUser>\n"
            + "        <kanaName>なぶらくじろう</kanaName>\n"
            + "        <kanjiName>名部楽次郎</kanjiName>\n"
            + "        <userId>2</userId>\n"
            + "    </sampleUser>\n"
            + "</userList>\n";

    /**
     * 正常終了のテストケース。
     * レスポンスがJSON
     */
    @Test
    public void testFindJson() {
        String message = "ユーザー一覧取得（JSON）";
        HttpResponse response = sendRequest(get("/find/json"));
        assertStatusCode(message, HttpResponse.Status.OK.getStatusCode(), response);

        try {
            JSONAssert.assertEquals(message, JSON_RESPONSE
                    , response.getBodyString(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            fail(e.getMessage());
        }
    }

    /**
     * 正常終了のテストケース。
     * レスポンスがXML
     */
    @Test
    public void testFindXml() {
        String message = "ユーザー一覧取得（XML）";
        HttpResponse response = sendRequest(get("/find/xml"));
        assertStatusCode(message, HttpResponse.Status.OK.getStatusCode(), response);
        assertThat(Input.fromString(response.getBodyString()), isSimilarTo(Input.fromString(XML_RESPONSE)));
    }
}
