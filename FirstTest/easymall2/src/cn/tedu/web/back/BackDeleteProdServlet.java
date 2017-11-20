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
 * 从商品列表中昂删除商品的servlet
 * Created by tarena on 2016/9/7.
 */
@WebServlet(name = "BackDeleteProdServlet")
public class BackDeleteProdServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取要删除的商品的id
        String id=request.getParameter("id");
        try{
            ProdService service= BasicFactory.getFactory().getInstance(ProdService.class);
            //删除商品
            service.deleteProd(id);
            response.getWriter().write("删除成功，2秒后跳转到商品列表，如果没有跳转请点击下面的链接跳转：</br>" +
                    "<a href="+request.getContextPath()+"/BackProdListServlet>跳转</a>");
        }catch(Exception e){
            response.getWriter().write("删除失败，2秒后跳转到商品列表，如果没有跳转请点击下面的链接跳转：</br>" +
                    "<a href="+request.getContextPath()+"/BackProdListServlet>跳转</a>");
        }
        response.setHeader("refresh","2;url="+request.getContextPath()+"/BackProdListServlet");
    }
}
