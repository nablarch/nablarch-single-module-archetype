package nablarch.etl;

import nablarch.core.db.connection.AppDbConnection;
import nablarch.core.db.connection.DbConnectionContext;
import nablarch.core.db.statement.SqlPStatement;
import nablarch.core.util.StringUtil;
import nablarch.etl.config.EtlConfig;
import nablarch.etl.config.RootConfig;
import nablarch.etl.config.TruncateStepConfig;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Table;
import java.util.List;

/**
 * テーブルのデータをクリーニング(truncate)する{@link nablarch.etl.TableCleaningBatchlet}のDB2対応版。
 * <p/>
 * 機能の詳細は{@link nablarch.etl.TableCleaningBatchlet}を参照すること。
 *
 */
@Named
@Dependent
public class TableCleaningBatchletForDb2 extends AbstractBatchlet {

    /** {@link JobContext} */
    @Inject
    private JobContext jobContext;

    /** {@link StepContext} */
    @Inject
    private StepContext stepContext;

    /** ETLの設定 */
    @Inject
    @EtlConfig
    private RootConfig rootConfig;


    /**
     * etl.jsonのtruncateに指定されたエンティティクラスのテーブルに対して
     * truncateを実行する。
     * <p/>
     * 指定したエンティティクラスに{@link javax.persistence.Table}が無い場合、
     * もしくはテーブル名の指定が無い場合はそのエンティティクラスを無視する。
     *
     * @return 処理が成功した際に"SUCCESS"を返す。
     * @throws Exception 処理中にエラーが起こった場合。
     */
    @Override
    public String process() throws Exception {

        final TruncateStepConfig config = rootConfig.getStepConfig(jobContext.getJobName(), stepContext.getStepName());
        final List<Class<?>> entities = config.getEntities();

        final AppDbConnection connection = DbConnectionContext.getConnection();
        for (Class<?> entity : entities) {
            String tableNameWithSchema = getTableNameWithSchema(entity);
            if (StringUtil.hasValue(tableNameWithSchema)) {
                final SqlPStatement statement = connection.prepareStatement(
                        "truncate table " + tableNameWithSchema + " immediate" );
                statement.execute();
            }
        }
        return "SUCCESS";
    }

    /**
     * エンティティクラスからテーブル名を取得する。
     * スキーマの指定がある場合、スキーマ名.テーブル名の形式で
     * 取得する。
     * <p/>
     * 引数に渡されたクラスに{@link javax.persistence.Table}が無い場合は
     * 空文字列を返す。
     *
     * @param entity テーブル名を取得するエンティティクラス。
     * @return テーブル名。
     */
    private String getTableNameWithSchema(Class<?> entity){
        final Table table = entity.getAnnotation(Table.class);
        if (table == null) {
            return "";
        }
        if (StringUtil.isNullOrEmpty(table.schema())) {
            return table.name();
        } else {
            return table.schema() + '.' + table.name();
        }
    }
}
