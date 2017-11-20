package August_23;

import java.sql.*;

/**
 * Created by tarena on 2016/8/23.
 */
public class JDBCPoolTest {
    public static void main(String[] args) {
        Connection conn=null;
        Statement stat=null;
        ResultSet rs=null;
        JDBCPool jp=new JDBCPool();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn=jp.getConnection();
            stat=conn.createStatement();
            stat.executeUpdate("insert into user values('gao','123')");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
                jp.returnPool(conn);
        }
    }
}
