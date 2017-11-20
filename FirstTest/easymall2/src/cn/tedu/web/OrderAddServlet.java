package cn.tedu.web;

import cn.tedu.domain.Order;
import cn.tedu.domain.OrderItem;
import cn.tedu.domain.Product;
import cn.tedu.domain.User;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by tarena on 2016/9/9.
 */
@WebServlet(name = "OrderAddServlet",urlPatterns = {"/OrderAddServlet"})
public class OrderAddServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user=(User)request.getSession().getAttribute("user");
        if(user==null){
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return;
        }
        //
        Map<Product,Integer> cart=(Map<Product, Integer>) request.getSession().getAttribute("cart");
        if(cart==null){
            response.sendRedirect(request.getContextPath()+"/cart.jsp");
        }
        Order order=new Order();
        order.setId(UUID.randomUUID().toString());//订单id
        order.setOrdertime(new Date()); //订单生成日期
        order.setPaystate(0); //订单状态
        order.setUser_id(user.getId());//用户id

        double money=0;
        List<OrderItem> list=new ArrayList<>();
        for(Map.Entry<Product,Integer> prod:cart.entrySet()){
            OrderItem oi=new OrderItem();
            oi.setBuynum(prod.getValue());
            oi.setOrder_id(order.getId());
            oi.setProdect_id(prod.getKey().getId());

            list.add(oi);
            money+=prod.getKey().getPrice()*prod.getValue();
        }
        order.setMoney(money);//订单总金额
        /*调用service中的order*/
        OrderService service= BasicFactory.getFactory().getInstance(OrderService.class);
        try {
            service.addOrder(order, list);
        /*清空购物车*/
          //  cart.clear();
            request.setAttribute("order", order);

            request.getRequestDispatcher(request.getContextPath() + "/orderList.jsp").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
            request.getRequestDispatcher(request.getContextPath() + "/cart.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
    }
}
