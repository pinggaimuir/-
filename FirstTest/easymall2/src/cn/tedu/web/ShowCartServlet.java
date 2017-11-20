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
 * 查看购物车的Servlet
 * Created by tarena on 2016/9/8.
 */
@WebServlet(name = "ShowCartServlet")
public class ShowCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       Map<Product,Integer> cart= (Map) request.getSession(false).getAttribute("cart");
//        for(Map.Entry<Product,Integer> entry:cart.entrySet()){
//
//        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
