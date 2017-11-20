package cn.tedu.web;

import cn.tedu.factory.BasicFactory;
import cn.tedu.service.ProdService;
import cn.tedu.utils.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/9/7.
 */
@WebServlet(name = "ProdPageServlet")
public class ProdPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收当前页码参数
        int thispage=Integer.parseInt(request.getParameter("thispage"));
        int rowperpage=Integer.parseInt(request.getParameter("rowperpage"));
        //获得商品查询的参数
        String nameStr=request.getParameter("name");
        String categoryStr=request.getParameter("category");
        String minStr=request.getParameter("minprice");
        String maxStr=request.getParameter("maxprice");

        String name="";
        String category="";
        if(nameStr!=null&&!"".equals(nameStr)){
            name=nameStr;
        }
        if(categoryStr!=null&&!"".equals(categoryStr)){
            category=categoryStr;
        }
        double min=-1;
        double max=Double.MAX_VALUE;
        if(minStr!=null&&!"".equals(minStr)){
             min =Double.parseDouble(minStr);
        }
        if(maxStr!=null&&!"".equals(maxStr)){
             max=Double.parseDouble(maxStr);
        }
        ProdService service= BasicFactory.getFactory().getInstance(ProdService.class);
        Page page=service.pageList(thispage,rowperpage,name,category,min,max);
        request.setAttribute("page",page);
        request.setAttribute("name",name);
        request.setAttribute("category",category);
        if(min!=-1){
            request.setAttribute("minprice",min);
        }
        if(max!=Double.MAX_VALUE){
            request.setAttribute("maxprice",max);
        }
        request.getRequestDispatcher("/prodlist.jsp").forward(request,response);
    }
}
