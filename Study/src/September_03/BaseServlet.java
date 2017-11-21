package September_03;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by gao on 2016/9/8.
 */
@WebServlet(name = "BaseServlet",urlPatterns = {"/BaseServlet"})
public class BaseServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methodName=request.getParameter("method");
        if(methodName==null||methodName.trim().isEmpty()){
            throw new RuntimeException("您没有传递方法，我很生气！");
        }
        Class c=this.getClass();
        Method method=null;
        try {
            method= c.getMethod(methodName,HttpServletRequest.class,HttpServletRequest.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("您调用的方法不存在！");
        }

        try{
           String result= (String) method.invoke(this,request,response);
            if(result==null||result.trim().isEmpty()){
                return;
            }
            if(result.contains(":")){
                String s=result.split(":")[0];
                String f=result.split(":")[1];
                if(s.equalsIgnoreCase("r")){
                    response.sendRedirect(request.getContextPath()+f);
                }else if(s.equalsIgnoreCase("f")){
                    request.getRequestDispatcher(f).forward(request,response);
                }
            }else{
                request.getRequestDispatcher(result).forward(request,response);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("wo shi get");
    }
    void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("wo shi add");
    }
}
