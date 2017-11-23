package August_27;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by tarena on 2016/8/27.
 */
@WebServlet(name = "ServletSetHeaders")
public class ServletSetHeaders extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

       // response.setHeader("refresh","3;URL='http://www.4399.com'");
        //设置状态码
        response.setStatus(200);
        //设置响应头的解码方式
       // response.setHeader("Content-Encoding","gzip");
        //通知客户端以下载的方式接收数据
       // response.setHeader("Content-Disposition","attachment,filename=1.jpg");

        response.setHeader("Content-Type","text/html;charset=gbk");

/*        //设置浏览器不要缓存
        response.setDateHeader("Expires",-1);
        response.setHeader("Chche-Control","no-cache");
        response.setHeader("Pragma","no-cache");*/

        //设置浏览器缓存
        response.setDateHeader("Expires",System.currentTimeMillis()+1000*60*60*1);
        response.setHeader("Chche-Control","max-age=60");

        Date date=new Date();
       // response.getWriter().write(date.toLocaleString()+"时间");
        //设置刷新
        response.setHeader("refresh","5;url=http://www.tmooc.com");
       // response.sendRedirect("http://www.4399.com");
        //response.setHeader("Location","http://www.tmooc.cn");
       // response.flushBuffer();

    }
}
