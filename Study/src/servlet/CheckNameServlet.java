package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gao on 2016/9/16.
 */
@WebServlet(name = "CheckNameServlet",urlPatterns = {"/CheckNameServlet"})
public class CheckNameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        String name=request.getParameter("name");
        System.out.println(name);
        if(name==null){
            System.out.println("name 不能为空！");
        }
        if(name=="gao"){
            response.getWriter().write("您的名户名已经被存在");
        }else{
            response.getWriter().write("用户名可以使用！");
        }

    }
}
