package test3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by tarena on 2016/9/9.
 */
@WebServlet(name = "BaseServlet")
public class BaseServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method=request.getParameter("method");
        if(method==null||method.trim().isEmpty()){
            throw new RuntimeException("您没有输入method方法或者输入不正确！");
        }
        Class c=this.getClass();
        Method method1=null;
        try {
             method1=c.getMethod(method,HttpServletRequest.class,HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("您输入的方法"+method+"不存在!");
        }
        if(method!=null){
            try {
                String msg= (String) method1.invoke(this,request,response);
                if(msg.contains(":")){
                    String msg1=msg.split(":")[0];
                    String msg2=msg.split(":")[1];
                    if("r".equalsIgnoreCase(msg1)){
                        response.sendRedirect(request.getContextPath()+msg2);
                    }else if("f".equalsIgnoreCase(msg1)){
                        request.getRequestDispatcher(request.getContextPath()+msg2).forward(request,response);
                    }
                }else{
                    request.getRequestDispatcher(request.getContextPath()+msg).forward(request,response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
