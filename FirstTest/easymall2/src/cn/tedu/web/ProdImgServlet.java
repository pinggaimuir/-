package cn.tedu.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 通过转发的方式显示WEN-INF中的图片
 * Created by tarena on 2016/9/6.
 */
@WebServlet(name = "ProdImgServlet")
public class ProdImgServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imgurl=request.getParameter("imgurl");
        request.getRequestDispatcher(imgurl).forward(request,response);
    }
}
