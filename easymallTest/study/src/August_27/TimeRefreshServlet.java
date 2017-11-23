package August_27;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 从cookie中获得上次访问时间
 * Created by tarena on 2016/8/30.
 */
@WebServlet(name = "TimeRefreshServlet",urlPatterns={"/TimeRefreshServlet"})
public class TimeRefreshServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        Cookie cookie=new Cookie("time",new Date().toLocaleString()+"");
        cookie.setPath(request.getContextPath()+"/");
        response.addCookie(cookie);
        Cookie[] cookies=request.getCookies();
        String times=null;
        if(cookies!=null){
            for(Cookie c:cookies){
                if("time".equals(c.getName())){
                    response.getWriter().write("上次访问的时间位为"+c.getValue());
                }
            }
        }else{
            response.getWriter().write("欢迎第一次访问本网页！");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
    }
}
