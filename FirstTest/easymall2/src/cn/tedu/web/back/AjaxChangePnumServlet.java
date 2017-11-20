package cn.tedu.web.back;

import cn.tedu.factory.BasicFactory;
import cn.tedu.service.ProdService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/9/7.
 */
@WebServlet(name = "AjaxChangePnumServlet")
public class AjaxChangePnumServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String id = request.getParameter("id");//获取要修改的商品id
            int pnum = Integer.parseInt(request.getParameter("pnum"));//获取要修改的商品的数量
            ProdService service = BasicFactory.getFactory().getInstance(ProdService.class);
            service.updatePnum(id, pnum);
            response.getWriter().write("true");
        }catch(Exception e){
            response.getWriter().write("false");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
