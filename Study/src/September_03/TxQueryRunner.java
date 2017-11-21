package September_03;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.SQLException;

/**
 * Created by gao on 2016/9/9.
 */
public class TxQueryRunner extends QueryRunner {
    @Override
    public int[] batch(String sql, Object[][] params) throws SQLException {
        return super.batch(sql, params);
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return super.query(sql, rsh, params);
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        return super.query(sql, rsh);
    }

    @Override
    public int update(String sql) throws SQLException {
        return super.update(sql);
    }

    @Override
    public int update(String sql, Object param) throws SQLException {
        return super.update(sql, param);
    }

    @Override
    public int update(String sql, Object... params) throws SQLException {
        return super.update(sql, params);
    }
}
