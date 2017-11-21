package servlet1;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Created by gao on 2016/8/29.
 */
@WebServlet(name = "c3p0jdbcServlet")
public class c3p0jdbcServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ComboPooledDataSource ds=new ComboPooledDataSource();
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
