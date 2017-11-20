package August_23;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by tarena on 2016/8/23.
 */
public class JDBCc3p0Test {
    public static void main(String[] args) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        Properties prop=new Properties();
        //InputStream in=JDBCc3p0Test.class.getResourceAsStream("../c3p0.properties");
        ComboPooledDataSource cpds=new ComboPooledDataSource();
        try {
            //String path=JDBCc3p0Test.class.getResource("../c3p0.properties").getPath();
           // prop.load(in);
//            cpds.setJdbcUrl(prop.getProperty("c3p0.jdbcUrl"));
//            cpds.setDriverClass(prop.getProperty("c3p0.driverClass"));
//            cpds.setUser(prop.getProperty("c3p0.user"));
//            cpds.setPassword(prop.getProperty("c3p0.password"));
            conn=cpds.getConnection();
            String sql="select * from user where username=?";
            ps=  conn.prepareStatement(sql);
            //  rs=stat.executeQuery("select *  from user where username='"+username+"'and password='"+password+"'");
            ps.setString(1,"gao");
           // ps.setString(2,password);
            rs=ps.executeQuery();
            if(rs.next()){
                System.out.println(rs.getString(1)+"  ----"+rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
//        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally{
            BaseConnection.close(conn,ps,rs);
        }
    }
}
