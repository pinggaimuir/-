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

/**
 * Created by tarena on 2016/9/8.
 */
@WebServlet(name = "ProdInfoServlet",urlPatterns = {"/ProdInfoServlet"})
public class ProdInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("id");
        ProdService service=BasicFactory.getFactory().getInstance(ProdService.class);
        Product prod=service.findProdById(id);
        request.setAttribute("prod",prod);
        request.getRequestDispatcher(request.getContextPath()+"/prodinfo.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
    }
}
