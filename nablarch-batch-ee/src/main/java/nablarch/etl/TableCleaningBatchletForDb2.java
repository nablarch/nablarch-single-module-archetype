package nablarch.etl;

import nablarch.common.dao.EntityUtil;
import nablarch.core.db.connection.AppDbConnection;
import nablarch.core.db.connection.DbConnectionContext;
import nablarch.core.db.statement.SqlPStatement;
import nablarch.etl.config.EtlConfig;
import nablarch.etl.config.StepConfig;
import nablarch.etl.config.TruncateStepConfig;

import javax.batch.api.AbstractBatchlet;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
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

    /** ETLの設定 */
    private final TruncateStepConfig stepConfig;

    /**
     * コンストラクタ。
     *
     * @param stepConfig ステップの設定
     */
    @Inject
    public TableCleaningBatchletForDb2(@EtlConfig final StepConfig stepConfig) {
        this.stepConfig = (TruncateStepConfig) stepConfig;
    }


    @Override
    public String process() throws Exception {

        final List<Class<?>> entities = stepConfig.getEntities();

        final AppDbConnection connection = DbConnectionContext.getConnection();
        for (Class<?> entity : entities) {
            final SqlPStatement statement = connection.prepareStatement(
                    "truncate table " + EntityUtil.getTableNameWithSchema(entity) + " immediate");
            statement.execute();
        }

        return "SUCCESS";
    }
}
