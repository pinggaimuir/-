package test3;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/9/9.
 */
public class tansaction {
   private static ComboPooledDataSource source=new ComboPooledDataSource();
    public static ComboPooledDataSource getSoucre(){
        return source;
    }

    private  static Connection con;

    public static Connection getCon() throws SQLException {
        if(con!=null)return con;
        return source.getConnection();
    }

    public static void startTransaction() throws SQLException {
        try {
            con=getCon();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(false);
    }

    public static void commitTranscation() throws SQLException {
        if(con==null)throw new SQLException("不要重复提交事物！");
        con.commit();
        con.close();
        con=null;
    }

    public static void rollbackTransaction() throws SQLException {
        if(con==null)throw  new SQLException("还有没开启事物，不能提交！");
        con.rollback();
        con.close();
        con=null;
    }

    public static void releaseTransaction(Connection connection) throws SQLException {
        if(con==null)connection.close();
        if(con!=connection)connection.close();
    }
}
