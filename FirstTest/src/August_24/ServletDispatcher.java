package August_24;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/8/26.
 */
@WebServlet(name = "ServletDispatcher")
public class ServletDispatcher extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //response.setContentType("text/html;charset=utf-8");

        String uname=request.getParameter("uname");
        String upwd=request.getParameter("upwd");
//        request.setAttribute("uname",uname);
//        request.setAttribute("upwd",upwd);

        response.getWriter().write("servletDispatcher1:"+uname+"<br/>");
        //刷新流
       // response.flushBuffer();
        request.setAttribute("li","ning");
        RequestDispatcher rd=request.getRequestDispatcher("/servletTest");
        rd.forward(request,response);
/*      不能进行多次转发
        RequestDispatcher rs=request.getRequestDispatcher("/LoginServlet");
        rs.forward(request,response);
        */



    }
}
