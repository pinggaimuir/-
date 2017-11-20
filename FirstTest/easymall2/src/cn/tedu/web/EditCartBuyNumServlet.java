package cn.tedu.web;

import cn.tedu.domain.Product;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.ProdService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by tarena on 2016/9/8.
 */
@WebServlet(name = "EditCartBuyNumServlet",urlPatterns = {"/EditCartBuyNumServlet"})
public class EditCartBuyNumServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*接收参数*/
            String id=request.getParameter("id");
            int cartNum=Integer.parseInt(request.getParameter("cartNum"));
        /*从session总获取购物车*/
        Map<Product, Integer> cart = (Map<Product, Integer>) request.getSession(false).getAttribute("cart");
/*
        if(cart!=null) {
            for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                if (id.equals(entry.getKey().getId())) {
                    cart.put(entry.getKey(), cartNum);
                }
            }
            request.getRequestDispatcher(request.getContextPath()+"/cart.jsp").forward(request,response);
        }else{
            throw new RuntimeException("session已经消亡");
        }
*/

        ProdService service= BasicFactory.getFactory().getInstance(ProdService.class);
        Product prod=null;
        try {
            prod = service.findProdById(id);
            /*修改购物车中商品数量*/
            cart.put(prod, cartNum);
        }catch (Exception e){
            e.printStackTrace();
            Product pd=new Product();
            pd.setId(id);
            cart.remove(pd);//从购物车中删除已经下架的商品
            request.setAttribute("msg",e.getMessage());
        }
        request.getRequestDispatcher(request.getContextPath()+"/cart.jsp").forward(request,response);
    }
}
