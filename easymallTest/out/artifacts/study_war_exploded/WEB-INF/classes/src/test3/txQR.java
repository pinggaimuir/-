package test3;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/9/10.
 */
public class txQR extends QueryRunner {
    @Override
    public int[] batch(String sql, Object[][] params) throws SQLException {
       Connection con= TxManager.getCon();
        int[] result= super.batch(con,sql, params);
        TxManager.relaseTransaction(con);
        return result;
    }

    @Override
    public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh) throws SQLException {
        return super.query(conn, sql, rsh);
    }

    @Override
    public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return super.query(conn, sql, rsh, params);
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        return super.query(sql, rsh);
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return super.query(sql, rsh, params);
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
