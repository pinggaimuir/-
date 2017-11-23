package August_27;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/9/1.
 */
@WebServlet(name = "CartSessionServlet",urlPatterns={"/CartSessionServlet"})
public class CartSessionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");


        String name=(String)request.getSession().getAttribute("name");
        if(name!=null){
            response.getWriter().write("对您购买的"+name+"进行结账！,共支付了10000原");
        }else{
            response.getWriter().write("您的购物车没有物品");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
