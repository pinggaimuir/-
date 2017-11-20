package August_23;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by tarena on 2016/8/23.
 */
public class JDBCPool implements DataSource {
    //易于增删
    static List<Connection> list=new LinkedList<Connection>();
    static{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            for(int i=0;i<5;i++){
                Connection conn= DriverManager.getConnection("jdbc:mysql:///mydb2","root","root");
                list.add(conn);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Connection getConnection() throws SQLException {
        //如果连接池中的连接数不够则新增
        if(list.isEmpty()){
            for(int i=0;i<3;i++){
                Connection conn=DriverManager.getConnection("jdbc:mysql:///mydb2","root","root");
                list.add(conn);
            }
        }
        //删除连接池中的一个连接 并返回
        Connection conn=list.remove(0);
        System.out.println("连接初始化成功！池中还剩"+list.size()+"个连接");
        return conn;
    }
    /*程序将不用的连接归还*/
    public void returnPool(Connection conn){
        try {
            if(conn!=null&&!conn.isClosed()){
                list.add(conn);
                System.out.println("成功的返回了一个连接，池中连接数为："+list.size());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
