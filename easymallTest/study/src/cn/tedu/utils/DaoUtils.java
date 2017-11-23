package cn.tedu.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.*;

/**
 * Created by tarena on 2016/9/3.
 */
public class DaoUtils {
    private static ComboPooledDataSource source=new ComboPooledDataSource();
    private DaoUtils(){}

    /**
     * 返回数据原
     * @return
     */
    public static ComboPooledDataSource getSource(){
        return source;
    }

    /**
     * 从数据源获取链接
     * @return 数据库链接
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    public static void update(String sql,Object...params){
        MyQueryRunner qr=new MyQueryRunner(source);
        qr.update(sql,params);
    }

    public static <T>T query(String sql,MyResultSetHandler<T> rs,Object...params){
        MyQueryRunner qr=new MyQueryRunner(source);
        return qr.query(sql,rs,params);
    }
    /**
     * 关闭数据库资源
     * @param rs ResultSet
     * @param stat Statement
     * @param conn Connection
     */
    public static void close(ResultSet rs,Statement stat,Connection conn){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                rs=null;
            }
        }
        if(stat!=null){
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                stat=null;
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                conn=null;
            }
        }
    }
}
