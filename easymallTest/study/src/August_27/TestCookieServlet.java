package August_27;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by tarena on 2016/9/1.
 */
@WebServlet(name = "TestCookieServlet",urlPatterns={"/CookieServlet"})
public class TestCookieServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        HttpSession session=request.getSession();
        session.setAttribute("name","高健");

        Cookie cookie=new Cookie("JSESSIONID",session.getId());
        cookie.setPath(request.getContextPath()+"/");
        cookie.setMaxAge(60*30);
        response.addCookie(cookie);
        response.getWriter().write("成功将高健加入购物车！");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doPost(request,response);
    }
}
