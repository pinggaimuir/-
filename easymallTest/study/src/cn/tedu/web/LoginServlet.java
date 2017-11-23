package cn.tedu.web;

import cn.tedu.domain.User;
import cn.tedu.exception.MsgException;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.UserService;
import cn.tedu.utils.MD5Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by tarena on 2016/8/30.
 */
@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("utf-8");
//        response.setContentType("text/html;charset=utf-8");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String remname=request.getParameter("remname");

        if("true".equals(remname)){
            //要求浏览器创建cookie存储用户名
            Cookie remnameCookie=new Cookie("username", URLEncoder.encode(username,"utf-8"));
            remnameCookie.setPath(request.getContextPath()+"/");
            remnameCookie.setMaxAge(60*60);
            response.addCookie(remnameCookie);
        }else{//删除cookie
            Cookie remnameCookie=new Cookie("username","");
            remnameCookie.setPath(request.getContextPath()+"/");
            remnameCookie.setMaxAge(0);
            response.addCookie(remnameCookie);
        }

        //根据用户名密码登陆
        try {
            UserService userService= BasicFactory.getFactory().getInstance(UserService.class);
            /*对密码进行md5加密*/
            password= MD5Utils.md5(password);
            User user=userService.login(username,password);
            if(user==null){
                    throw new MsgException("用户名或密码错误");
            }
            //登陆成功保存用户到session,用于登陆后显示用户名登陆状态
            request.getSession().setAttribute("user",user);
              /*
        *设置30天自动登陆
         *  */
            if("true".equals(request.getParameter("autologin"))){
                Cookie autoCookie=new Cookie("autologin",URLEncoder.encode(username+":"+password,"utf-8"));
                autoCookie.setPath(request.getContextPath()+"/");
                autoCookie.setMaxAge(60*60*24*30);
                response.addCookie(autoCookie);
            }else{
/*                Cookie autoCookie=new Cookie("UnamePwd","");
                autoCookie.setPath(request.getContextPath()+"/");
                autoCookie.setMaxAge(0);
                response.addCookie(autoCookie);*/
            }

            //输出登陆成功信息，并且在3秒后跳转到到首页
            response.getWriter().write("恭喜您，登陆成功！3秒后跳转到首页。若无跳转请点击下面的链接：" +
                                                "<br/><br/><a href="+request.getContextPath()+"/index.jsp>http://www.easymall.com</a>");
            response.setHeader("Refresh","3;url="+request.getContextPath()+"/index.jsp");
        } catch (MsgException e) {
                request.setAttribute("msg",e.getMessage());
                request.getRequestDispatcher(request.getContextPath()+"/login.jsp").forward(request,response);
        }catch(Exception e1){
            e1.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
