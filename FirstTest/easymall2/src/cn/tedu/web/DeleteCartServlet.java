package cn.tedu.web;

import cn.tedu.domain.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 更新购物车总的商品数量
 * Created by tarena on 2016/9/8.
 */
@WebServlet(name = "DeleteCartServlet",urlPatterns = {"/DeleteCartServlet"})
public class DeleteCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("id");
        Map<Product,Integer> cart= (Map<Product, Integer>) request.getSession(false).getAttribute("cart");
        /*遍历并删除购物车map中的商品*/
        if(cart!=null) {
        Product temp = null;
            for (Product product : cart.keySet()) {
                if (id.equals(product.getId())) {
                    temp = product;
                }
            }
            cart.remove(temp);
//            Product prod=new Product();
//            prod.setId(id);
//            cart.remove(prod);
        }

        request.getRequestDispatcher(request.getContextPath()+"/cart.jsp").forward(request,response);
    }
}
