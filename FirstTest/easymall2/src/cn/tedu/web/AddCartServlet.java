package cn.tedu.web;

import cn.tedu.domain.Product;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.ProdService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tarena on 2016/9/8.
 */
@WebServlet(name = "AddCartServlet",urlPatterns = {"/AddCartServlet"})
public class AddCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String id = request.getParameter("id");
            int num = Integer.parseInt(request.getParameter("num"));
            ProdService service = BasicFactory.getFactory().getInstance(ProdService.class);
        /*通过id查询到商品信息对象*/
            Product prod = service.findProdById(id);
        /*创建map存储商品对象和添加购物车数量*/
            Map<Product, Integer> cart = null;
            if (request.getSession(false) != null) {
                cart = (Map<Product, Integer>) request.getSession(false).getAttribute("cart");
                if (cart == null) {
                    cart = new HashMap<>();
                }
            }
        /*    boolean flag = true;
            Product temp = null;

            if (!cart.isEmpty()) {
                for (Product product : cart.keySet()) {
                    if (product.getId().equals(prod.getId())) {
                        flag = false;
                        temp = product;
                    }
                }
                if (flag) {
                    cart.put(prod, num);
                } else {
                    cart.put(temp, cart.get(temp) + num);
                }
            } else {
                cart.put(prod, num);
            }*/
            if(cart.containsKey(prod)){
                cart.put(prod,cart.get(prod)+num);
            }else{
                cart.put(prod,num);
            }
            HttpSession cartSession = request.getSession();
            cartSession.setAttribute("cart", cart);
        /*创建cookie保存jsessonid在客户端的cookie中*/
            Cookie cookie = new Cookie("JSESSIONID", cartSession.getId());
            cookie.setPath(request.getContextPath() + "/");
            cookie.setMaxAge(60 * 30);
            response.addCookie(cookie);
            response.getWriter().write("true");
        }catch (Exception e){
            response.getWriter().write("false");
        }
        /*response.getWriter().write("添加购物城成功！2秒后返回商品列表!点击下面链接可以查看购物车：" +
              "<a href="+request.getContextPath()+"/cart.jsp>购物车</a>");
        response.setHeader("refresh","2;url="+request.getContextPath()+"/ProdPageServlet?thispage=1&rowperpage=10");*/
    }
}
