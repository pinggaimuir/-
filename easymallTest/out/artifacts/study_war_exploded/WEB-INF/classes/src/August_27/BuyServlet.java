package August_27;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by tarena on 2016/8/30.
 */
@WebServlet(name = "BuyServlet",urlPatterns={"/BuyServlet"})
public class BuyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
//        String prop=request.getParameter("prop");
//        prop=new String(prop.getBytes("iso-8859-1"),"utf-8");
        HttpSession session=request.getSession();
        session.setAttribute("prop","阿迪王");

        Cookie cookie=new Cookie("JSESSIONID",session.getId());
        cookie.setPath(request.getContextPath()+"/");
        cookie.setMaxAge(60*30);
        response.addCookie(cookie);

        response.getWriter().write("成功将"+"大地网"+"加入了购物车！");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
