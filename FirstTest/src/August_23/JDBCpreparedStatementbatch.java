package August_23;

import java.sql.*;

/**
 * Created by tarena on 2016/8/23.
 */
public class JDBCpreparedStatementbatch {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb2", "root", "root");
            String sql = "insert into user values(?,?)";
            ps = conn.prepareStatement(sql);
            for(int i=0;i<500;i++){
                ps.setString(1, "name"+i);
                ps.setString(2, "password1"+i);
                ps.addBatch();
                if(i%100==0){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            ps.executeBatch();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseConnection.close(conn, ps, rs);
        }
    }
}
