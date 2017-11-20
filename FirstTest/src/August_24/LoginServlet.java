package August_24;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by tarena on 2016/8/24.
 */
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID= 654254534345453L;
//    @Override
//    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String userName=req.getParameter("uname");
//        String password=req.getParameter("upwd");
//        System.out.println("用户名=="+userName);
//        System.out.println("密码=="+password);
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.setCharacterEncoding("utf-8");
       // resp.setContentType("text/html;charset=UTF-8");
        System.out.println("======进入doGet方法======");
        String userName=req.getParameter("uname");
        String password=req.getParameter("upwd");
        //userName=new String(userName.getBytes("iso-8859-1"),"UTF-8");
        System.out.println("用户名=="+userName);
        System.out.println("密码=="+password);

        PrintWriter out=resp.getWriter();
        Map<String,String[]> map=req.getParameterMap();
        Set<Map.Entry<String,String[]>> set=map.entrySet();
        for(Map.Entry<String,String[]> entry:set){
           // entry.getValue()[0]=new String(entry.getValue()[0].getBytes("iso-8859-1"),"UTF-8");
            out.println("LongServlet3："+entry.getKey()+"------"+entry.getValue()[0]+"<br/>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("======进入doPost方法======");
        String userName=req.getParameter("uname");
        String password=req.getParameter("upwd");
        System.out.println("用户名=="+userName);
        System.out.println("密码=="+password);
        PrintWriter out=resp.getWriter();
       Map<String,String[]> map=req.getParameterMap();
        Set<Map.Entry<String,String[]>> set=map.entrySet();
        for(Map.Entry<String,String[]> entry:set){
            out.println("LongServlet3："+entry.getKey()+"------"+entry.getValue()[0]+"<br/>");
        }
        resp.setHeader("refresh","1");
        out.write("================<br/>");
        String ning=(String)req.getAttribute("li");
        out.write(ning+"<br/>");
        out.write("================<br/>");
        Date date=new Date();
        out.write("当前时间：--------------------------------"+date.toLocaleString()+"<br/>");


 /*       if(userName.equals("gao") && password.equals("jian")){
//            resp.sendRedirect(req.getContextPath()+"success.jsp");
            RequestDispatcher rd=req.getRequestDispatcher("success.jsp");
            rd.forward(req,resp);
        }else{
//            resp.sendRedirect(req.getContextPath()+"error.jsp");
            RequestDispatcher rd=req.getRequestDispatcher("error.jsp");
            rd.forward(req,resp);
        }*/
    }
}
