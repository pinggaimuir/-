package August_27;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/8/27.
 */
@WebServlet(name = "ExpirsServlet")
public class ExpirsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   /*     //设置浏览器不缓存
        response.setDateHeader("Expires",-1);//1970-1-1  时间不起作用  缓存无效
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Parema","nocache");//使用于http1.0*/
        //设置浏览器缓存
        response.setDateHeader("Expires",System.currentTimeMillis()+1000*60*60*1);
        response.setHeader("Cache-Control","max-age-60");
    }
}
