package cn.tedu.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/9/10.
 */
public class TransactionManager {
    private TransactionManager(){}
    private static ComboPooledDataSource source=new ComboPooledDataSource();
    public static ComboPooledDataSource getSoucre(){
        return source;
    }
    /*数据库抓用链接*/
    private  static ThreadLocal<Connection> tl=new ThreadLocal<Connection>();

    public static Connection getCon() throws SQLException {
        /*从Threadlocal中获取数据库链接*/
        Connection con=tl.get();
        /*保证事物开启后该专用链接不会被其他事物用到*/
        if(con!=null)return con;
        return source.getConnection();
    }

    /**
     * 开启事物
     * @throws SQLException
     */
    public static void startTransaction() throws SQLException {
        Connection con=tl.get();
        if(con!=null)throw new SQLException("已经开启事物，请不要重复开启！");
        con=getCon();
        con.setAutoCommit(false);
        tl.set(con);
    }

    /**
     * 提交事物
     * @throws SQLException
     */
    public static void commitTranscation() throws SQLException {
        Connection con=tl.get();
        if(con==null)throw new SQLException("事物还没有开启不能提交！");
        con.commit();
        con.close();
        tl.remove();
    }

    /**
     * 回滚事物
     * @throws SQLException
     */
    public static void rollbackTransaction() throws SQLException {
        Connection con=tl.get();
        if(con==null)throw  new SQLException("还有没开启事物，不能回滚！");
        con.rollback();
        con.close();
        tl.remove();
    }

    /**
     * 释放事物
     * @param connection
     * @throws SQLException
     */
    public static void releaseTransaction(Connection connection) throws SQLException {
        Connection con=tl.get();
        if(con==null)connection.close();//如果con为空，则connection肯定不是用于事物的，直接关闭
        if(con!=connection)connection.close();//如果con不等与connection则connection不是数据库专用链接，可以关闭
    }
}
