package cn.tedu.web;

import cn.tedu.domain.User;
import cn.tedu.exception.MsgException;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.UserService;
import cn.tedu.utils.MD5Utils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/8/27.
 */

public class RegistServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求参数
        User user=new User();
        try {
            BeanUtils.populate(user,request.getParameterMap());
            user.check();
            UserService userService= BasicFactory.getFactory().getInstance(UserService.class);
            /*对密码进行Md5加密*/
            user.setPassword(MD5Utils.md5(user.getPassword()));
            userService.regist(user);
        } catch (MsgException e) {
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher(request.getContextPath() + "/regist.jsp").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        response.getWriter().write("恭喜您注册成功，3秒后跳转到首页...<br/>" +
                "如没有跳转请点击下面的链接：<br/><a href="+request.getContextPath()+"'/index.jsp'>http://www.easymall.com<a/>");
        response.setHeader("refresh","3;url="+request.getContextPath()+"/index.jsp");


}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
