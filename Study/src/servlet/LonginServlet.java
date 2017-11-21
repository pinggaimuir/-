package servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gao on 2016/8/27.
 */
public class LonginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext sc=this.getServletContext();
        String encoding=sc.getInitParameter("encoding");
        //请求参数拜纳姆设置
        request.setCharacterEncoding(encoding);
        //相应输出编码设置
        response.setContentType("text/html;charset=utf-8");
        //获得参数
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String password2=request.getParameter("password2");
        String nickname=request.getParameter("nickname");
        String email=request.getParameter("email");
        String valistr=request.getParameter("valistr");
        //非空教研
        if(username==null||"".equals(username)){
            request.setAttribute("msg","用户名不能为空");
            request.getRequestDispatcher("/Regist.jsp").forward(request,response);
            return;
        }

        //邮箱格式检验
        if(!email.matches("^\\w+@\\w+(\\.\\w+)+$")){
            request.setAttribute("msg","邮箱格式不正确");
            request.getRequestDispatcher("/Regist.jsp").forward(request,response);
            return ;
        }
        if(password==null||"".equals(password)){
            request.setAttribute("msg","密码不能为空");
            request.getRequestDispatcher("/Regist.jsp").forward(request,response);
            return;
        }
        if(password2==null||"".equals(password2)){
            request.setAttribute("msg","确认密码不能为空");
            request.getRequestDispatcher("/Regist.jsp").forward(request,response);
            return;
        }

        //提示正确登录’
        //ServletContext scc=this.getServletContext();
        response.getWriter().write("恭喜你注册成功，3秒之后跳转到首页。。。");
        response.setHeader("Refresh","3;url="+request.getContextPath()+"/index.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
