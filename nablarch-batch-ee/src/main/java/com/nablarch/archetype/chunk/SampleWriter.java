package com.nablarch.archetype.chunk;

import com.nablarch.archetype.form.SampleUserForm;
import nablarch.common.databind.ObjectMapper;
import nablarch.common.databind.ObjectMapperFactory;

import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * 疎通確認用ItemWriter実装クラス。
 * ItemWriterが正常に動作することを確認する。
 * @deprecated TODO 疎通確認が終わったら削除すること。
 */
@Dependent
@Named
public class SampleWriter extends AbstractItemWriter {

    /** 出力先のファイルパス。 */
    private static final String OUTPUT_FILE_PATH =
            "testdata/output/outputdata.csv";

    /** データバインドに使用するObjectMapper。 */
    private ObjectMapper<SampleUserForm> mapper = null;

    @Override
    public void open(Serializable checkpoint) throws FileNotFoundException {
        mapper = ObjectMapperFactory.create(SampleUserForm.class,
                new FileOutputStream(OUTPUT_FILE_PATH));
    }

    /**
     * {@link SampleProcessor}が返却したformクラスを受け取り、
     * データバインドを使用しcsvに出力する。
     * @param o Processorで処理されたデータのリスト
     */
    @Override
    public void writeItems(List<Object> o) {
        for (Object data : o) {
            SampleUserForm form = (SampleUserForm) data;
            mapper.write(form);
        }
    }

    @Override
    public void close() {
        if (mapper != null) {
            mapper.close();
        }
    }
}
