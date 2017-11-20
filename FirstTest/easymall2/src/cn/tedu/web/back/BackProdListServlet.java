package cn.tedu.web.back;

import cn.tedu.domain.Product;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.ProdService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by tarena on 2016/9/6.
 */
@WebServlet(name = "BackProdListServlet")
public class BackProdListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProdService service= BasicFactory.getFactory().getInstance(ProdService.class);
        List<Product> list=service.prodList();
        request.setAttribute("list",list);
        request.getRequestDispatcher("/back/manageProd.jsp").forward(request,response);
    }
}
