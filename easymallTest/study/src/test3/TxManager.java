package test3;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/9/10.
 */
public class TxManager {
    private TxManager(){}
    private static ComboPooledDataSource source=new ComboPooledDataSource();

    public static ComboPooledDataSource getSource(){
        return source;
    }

    //数据库专链接
    private static ThreadLocal<Connection> tl=new ThreadLocal<Connection>();

      public static Connection getCon() throws SQLException {
          Connection con=tl.get();
          if(con!=null)return con;
        return source.getConnection();
    }

    public static void beginTransaction() throws SQLException {
        Connection con=tl.get();
        if(con!=null)throw new SQLException("不要重复开启数据库链接！");
        con.setAutoCommit(false);
        tl.set(con);
    }

    public static void commitTransaction() throws SQLException {
        Connection con=tl.get();
        if(con==null)throw new SQLException("事物还没有开启不能提交！");
        con.commit();
        con.close();
        tl.remove();
    }
    public static void rollbackTransaction() throws SQLException {
        Connection con=tl.get();
        if(con==null)throw new SQLException("事物还没有开启不能回滚！");
        con.rollback();
        con.close();
        tl.remove();
    }
    public static void relaseTransaction(Connection connection) throws SQLException {
        Connection con=tl.get();
        if(con==null)connection.close();
        if(con!=connection)connection.close();
    }
}
