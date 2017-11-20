package cn.tedu.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

    /**
     * 数据库的更新数据的方法
     * @param sql
     * @param params
     * @return
     */
    public static Number update(String sql,Object...params){
        QueryRunner qr=new QueryRunner(source);
        try {
             return (Number)qr.update(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询数据库的方法
     * @param sql 查询语句
     * @param t entity类的字节码文件
     * @param params 向查询语句中加入的参数列表
     * @param <T> 实体类的类型
     * @return 实体类对象
     */
    public static <T>T query(String sql,Class<T> t,Object...params){
        QueryRunner qr=new QueryRunner(source);
        try {
            return qr.query(sql,new BeanHandler<T>(t),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static <T>List<T> queryList(String sql, Class<T> t, Object...params){
        QueryRunner qr=new QueryRunner(source);
        try {
            return qr.query(sql,new BeanListHandler<T>(t),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static int queryCount(String sql,Object...params){
        QueryRunner qr=new QueryRunner(source);
        try {
            Number n= (Number)qr.query(sql,new ScalarHandler(),params);
            return n.intValue();
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }

    /**
     *数据跟新的方发，自己提供数据库链接
     * @param sql 跟新的sql语句
     * @param params 可变参数
     * @return 更新结果 int
     */
    public static void txbatch(String sql,Object[][] params){
        QueryRunner qr=new TxQueryRunner();
        try {
             qr.batch(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static int txupdate(String sql,Object...params){
        QueryRunner qr=new TxQueryRunner();
        try {
            return qr.update(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    /**
     * 数据查询的方法，返回结果为封装好的javabean
     * @param sql 数据库查询语句
     * @param t 用来封装数据的bean类的字节码
     * @param params 可变参数
     * @param <T>  用来封装数据的bean的类型
     * @return 封装了查询结果的bean对象
     */
    public static <T>T txquery(String sql,Class<T> t,Object...params){
        QueryRunner qr=new TxQueryRunner();
        try {
            return qr.query(sql,new BeanHandler<T>(t),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据查询的方法，自己提供数据库链接，返回结果为封装了结果集的对象的数组
     * @param sql 数据查询语言
     * @param t 封装结果集对象的类的字节码
     * @param params 可变参数
     * @param <T> 用来封装结果集的对象的类型
     * @return 封装了结果姐的对象的数组
     */
    public static <T>List<T> txqueryList(String sql,Class<T> t,Object...params){
        QueryRunner qr=new TxQueryRunner();
        try {
            return qr.query(sql,new BeanListHandler<T>(t),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
