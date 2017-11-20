package cn.tedu.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/9/10.
 */
public class TxQueryRunner extends QueryRunner {
    @Override
    public int[] batch(String sql, Object[][] params) throws SQLException {
        Connection con= TransactionManager.getCon();
        int[] result=super.batch(con,sql,params);
        TransactionManager.releaseTransaction(con);
        return result;
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        Connection con= TransactionManager.getCon();
        T t=super.query(con,sql,rsh);
        TransactionManager.releaseTransaction(con);
        return t;
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        Connection con= TransactionManager.getCon();
        T t=super.query(con,sql,rsh,params);
        TransactionManager.releaseTransaction(con);
        return t;
    }

    @Override
    public int update(String sql) throws SQLException {
        Connection con= TransactionManager.getCon();
        int n=super.update(con,sql);
        TransactionManager.releaseTransaction(con);
        return n;
    }

    @Override
    public int update(String sql, Object param) throws SQLException {
        Connection con= TransactionManager.getCon();
        int n=super.update(con,sql,param);
        TransactionManager.releaseTransaction(con);
        return n;
    }

    @Override
    public int update(String sql, Object... params) throws SQLException {
        Connection con= TransactionManager.getCon();
        int n=super.update(con,sql,params);
        TransactionManager.releaseTransaction(con);
        return n;
    }
}
