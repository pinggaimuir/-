package web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by tarena on 2016/9/25.
 */
@WebServlet(name = "CheckUsernameServlet",urlPatterns={"/CheckUsernameServlet"})
public class CheckUsernameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String username=request.getParameter("username");
//        username=new String(username.getBytes("iso-8859-1"),"utf-8");
        username= URLDecoder.decode(username,"utf-8");
        if(username==null||username.trim().isEmpty()){
            response.getWriter().write("用户名不能为空");
            return;
        }
      /*  if("gaojian".equals(username)){
            response.getWriter().write("用户名"+username+"已存在");
        }else{
            response.getWriter().write("恭喜你，用户名"+username+"可以使用！");
        }*/
        if("gaojian".equals(username)){
            response.getWriter().print("<font color='red'>用户名"+username+"已存在</font>");
        }else{
            response.getWriter().print("<font color='blue'>恭喜你，用户名"+username+"可以使用</font>！");
        }
    }
}
