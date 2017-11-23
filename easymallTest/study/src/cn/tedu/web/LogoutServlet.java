package cn.tedu.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销用户，并且返回首页
 * Created by tarena on 2016/8/30.
 */
@WebServlet(name = "LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession(false)!=null){
            //杀死Session,返回首页
            request.getSession().invalidate();
            //删除30天自动自动登陆
            Cookie autoCookie=new Cookie("autologin","");
            autoCookie.setPath(request.getContextPath()+"/");
            autoCookie.setMaxAge(0);
            response.addCookie(autoCookie);
        }
        response.sendRedirect(request.getContextPath()+"/index.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
