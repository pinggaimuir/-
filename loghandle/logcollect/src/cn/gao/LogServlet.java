package cn.gao;


import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 接收浏览器日志
 * Created by gao on 2017/1/8.
 */
@WebServlet(name = "LogServlet",urlPatterns={"/servlet/LogServlet"})
public class LogServlet extends HttpServlet {
    private static Logger logger=Logger.getLogger(LogServlet.class);
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取所有参数
        String str=request.getQueryString();

        //进行url解码
        str= URLDecoder.decode(str,"utf-8");
        //将str进行处理变成将值用|分割形式
        StringBuffer buffer=new StringBuffer();
        String [] kvs=str.split("&");
        for(String kv:kvs){
            String value=kv.split("=").length>=2?kv.split("=")[1]:"";
            buffer.append(value+"|");
        }
        //拼接ip
        String ip=request.getRemoteAddr();
        buffer.append(ip);
        str=buffer.toString();
        logger.info(str);
    }
}
