package cn.tedu.web;

import cn.tedu.utils.VerifyCode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/8/29.
 */
@WebServlet(name = "valiImageServlet")
public class ValiImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VerifyCode vc=new VerifyCode();

        vc.drawImage(response.getOutputStream());
        request.getSession().setAttribute("valistr",vc.getCode());
        //设置浏览器不缓存
        response.setDateHeader("Expires",-1);
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Pragma","no-cache");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
