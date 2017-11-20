package August_23;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/8/23.
 */
public class JDBCUtils {
    private JDBCUtils(){}
    //获得数据库的链接
    public static Connection getConnection(){
        Connection conn=null;
        ComboPooledDataSource cpds=new ComboPooledDataSource();
        try {
            conn=cpds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    //关闭数据链接
    public static void close(ResultSet rs, PreparedStatement ps,Connection conn){
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
