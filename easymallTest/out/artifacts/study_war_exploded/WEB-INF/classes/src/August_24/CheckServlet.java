package August_24;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CheckServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得一个CheckUserService的实例
        CheckUserService cku=new CheckUserService();
        String uname=request.getParameter("uname");
        String passwd=request.getParameter("upwd");

        RequestDispatcher rd=null;
        String forward=null;
        //判断登陆信息是否为空
        if(uname==null||passwd==null){
            request.setAttribute("msg","用户名获得密码不能为空！");
            rd=request.getRequestDispatcher("error.jsp");
            rd.forward(request,response);
        }else{
            User1 user=new User1();
            user.setName(uname);
            user.setPassword(passwd);

            //校验用户的登陆信息是否正确，并且返回结果
            Boolean flag=cku.check(user);
            if(flag){
                forward="success.jsp";
            }else{
                request.setAttribute("msg","用户或者密码输入错误，请重新输入！");
                forward="error.jsp";
            }
            rd=request.getRequestDispatcher(forward);
            rd.forward(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
