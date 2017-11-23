package cn.tedu.web;

import cn.tedu.domain.User;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户名是否已经存在
 * Created by tarena on 2016/8/29.
 */
@WebServlet(name = "CheckUserNameServlet")
public class CheckUserNameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名
        String username=request.getParameter("username");
        UserService userService= BasicFactory.getFactory().getInstance(UserService.class);
        User user=userService.findByUsername(username);
        response.getWriter().write((user!=null)+"");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
    }
}
