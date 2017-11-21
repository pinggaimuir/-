package September_03;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gao on 2016/9/3.
 */
@WebServlet(name = "AServlet",urlPatterns={"/AServlet"})
public class AServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String name=request.getParameter("name");
        System.out.println(name);
        response.getWriter().write(name);
        doPost(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String name=request.getParameter("name");
        System.out.println(name);
        response.getWriter().write(name);
    }
}
