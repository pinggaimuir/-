package test2;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JNDI配置
 * Created by tarena on 2016/9/5.
 */
@WebServlet(name = "JNDIServlet")
public class JNDIServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*        try {
            Context cxt=new InitialContext();
            Context envContext=(Context)cxt.lookup("java:comp/env");
            DataSource dataSource= (DataSource) envContext.lookup("jdbc/dataSource");
            Connection con=dataSource.getConnection();
            System.out.println(con);
            con.close();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
}
