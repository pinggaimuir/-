package August_23;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/8/29.
 */
public class JDBCUtilsTest {
    public static void main(String[] args) {
        Connection conn=null;
        PreparedStatement st=null;
        ResultSet rs=null;
        conn=JDBCUtils.getConnection();
        try {
            st=conn.prepareStatement("select * from user");
            rs=st.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(2)+"--------"+rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.close(rs,st,conn);
        }

    }
}
