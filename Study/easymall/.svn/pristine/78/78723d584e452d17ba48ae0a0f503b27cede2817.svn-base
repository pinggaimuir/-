package servlet1;

/**
 * Created by gao on 2016/8/28.
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1.解决乱码问题
        response.setContentType("text/html;charset=utf-8");
        // response.getWriter().write("恭喜您注册成功");
        //防止用户重复提交
        String token= (String) request.getSession().getAttribute("token");
        String token2=request.getParameter("token");
        if(token==null||token2==null||!token.equals(token2)){
            throw new RuntimeException("不要重复提交哦！");
        }else{
            request.getSession().removeAttribute("token");
        }
        // 2.获取请求参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");
        String valistr = request.getParameter("valistr");

        // 3.校验数据, 校验失败跳转回注册页面
        // 非空校验
        if (username == null || "".equals(username)) {
            request.setAttribute("msg", "用户名不能为空");
            request.getRequestDispatcher("/regist.jsp").forward(request,
                    response);
            return;
        }
        if (password == null || "".equals(password)) {
            request.setAttribute("msg", "密码不能为空");
            request.getRequestDispatcher("/regist.jsp").forward(request,
                    response);
            return;
        }
        if (password2 == null || "".equals(password2)) {
            request.setAttribute("msg", "确认密码不能为空");
            request.getRequestDispatcher("/regist.jsp").forward(request,
                    response);
            return;
        }
        // 其他校验
        if (!password.equals(password2)) {
            request.setAttribute("msg", "两次密码不一致");
            request.getRequestDispatcher("/regist.jsp").forward(request,
                    response);
            return;
        }

        if (nickname == null || "".equals(nickname)) {
            request.setAttribute("msg", "昵称不能为空");
            request.getRequestDispatcher("/regist.jsp").forward(request,
                    response);
            return;
        }
        if (email == null || "".equals(email)) {
            request.setAttribute("msg", "邮箱不能为空");
            request.getRequestDispatcher("/regist.jsp").forward(request,
                    response);
            return;
        }
        // 其他校验
        if (!email.matches("^\\w+@\\w+(\\.\\w+)+$")) {
            request.setAttribute("msg", "邮箱格式不正确");
            request.getRequestDispatcher("/regist.jsp").forward(request,
                    response);
            return;
        }
        if (valistr == null || "".equals(valistr)) {
            request.setAttribute("msg", "验证码不能为空");
            request.getRequestDispatcher("/regist.jsp").forward(request,
                    response);
            return;
        }

        // 4.用户名是否已经存在

        // 5.验证码是否正确

        // 6.将数据保存在数据库中 // TODO
        System.out.println("将数据保存在数据库中");

        // 7.注册成功给出提示信息, 3秒之后跳转到首页
        response.getWriter().write("恭喜你注册成功, 3秒之后跳转到首页......");
        response.setHeader("Refresh", "3;url=" + request.getContextPath()
                + "/index.jsp");

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
