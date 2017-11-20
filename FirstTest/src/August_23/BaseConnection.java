package August_23;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 获取JDBC连接的工具类
 * Created by tarena on 2016/8/23.
 */
public class BaseConnection {
    private BaseConnection(){}
    private  static Properties prop=new Properties();
    //从配置文件获取属性
    static{
        try {
            //用类加载器获取path
           String path= BaseConnection.class.getClassLoader().getResource("August_23/config1.properties").getPath();
            prop.load(new FileInputStream(new File(path)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //获取连接的方法
    public static Connection getConnection(){
        Connection conn=null;
        try {
            String driver=prop.getProperty("driver");
            String url=prop.getProperty("url");
            Class.forName(driver);
            conn= DriverManager.getConnection(url,"root","root");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
//    public static void main(String[] args){
//        Connection conn=BaseConnection.getConnection();
//        System.out.println(conn);
//    }
    //关闭连接的方法
    public static void close(Connection conn, PreparedStatement ps,ResultSet rs){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                conn=null;
            }
        }
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                ps=null;
            }
        }
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                rs=null;
            }
        }

    }

}
