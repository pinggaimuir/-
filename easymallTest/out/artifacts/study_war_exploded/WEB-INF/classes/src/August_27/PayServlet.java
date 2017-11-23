package August_27;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/8/30.
 */
@WebServlet(name = "PayServlet",urlPatterns={"/PayServlet"})
public class PayServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        String prop=(String)request.getSession().getAttribute("prop");
        if(prop!=null){
            response.getWriter().write("对您购买的"+prop+"进行结账！,共支付了10000原");
        }else{
            response.getWriter().write("您的购物车没有物品");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
