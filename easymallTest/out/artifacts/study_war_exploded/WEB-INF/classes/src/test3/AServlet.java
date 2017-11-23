package test3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/9/9.
 */
@WebServlet(name = "AServlet",urlPatterns = {"/AAAServlet"})
public class AServlet extends BaseServlet {
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("你调用了AddUser方法！");
        response.getWriter().write("你调用了AddUser方法！");
    }

    public  String  delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("你调用了Delete方法");
        response.getWriter().write("你调用了Delete方法！");
        return "r:/login.jsp";
    }
    public  String  update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("你调用了Update方法");
        response.getWriter().write("你调用了Update方法！");
        return "r:/login.jsp";
    }
}
