package servlet1;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by gao on 2016/8/29.
 */
public class c3p0 {
    public static void main(String[] args) {
        ComboPooledDataSource ds=new ComboPooledDataSource();
        System.out.println("test");
        Connection conn=null;
        PreparedStatement st=null;
        ResultSet rs=null;
        try {
            conn=ds.getConnection();
            st=conn.prepareStatement("select * from user");
            rs=st.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}