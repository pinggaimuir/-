package cn.tedu.filter;

import cn.tedu.dao.UserDao;
import cn.tedu.domain.User;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.UserService;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 *   30天内自动登录
 * Created by tarena on 2016/9/5.
 */

public class AutoLoginFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
         HttpServletRequest request=(HttpServletRequest)req;
         HttpServletResponse response=(HttpServletResponse)resp;
        //获得UserDaoImpl对象
        UserDao userDao= BasicFactory.getFactory().getInstance(UserDao.class);
        /*判断只有未登录状态才能自动登陆*/
        if(request.getSession(false)==null||request.getSession().getAttribute("user")==null){
            /*cookie中携带登陆信息才能登陆*/
            Cookie cs[]=request.getCookies();
            Cookie autoCookie=null;
            if(cs!=null){
                for(Cookie cookie:cs){
                    if("autologin".equals(cookie.getName())){
                        autoCookie=cookie;
                        break;
                    }
                }
            }

            if(autoCookie!=null){
                String unamePwd= URLDecoder.decode(autoCookie.getValue(),"utf-8");
                String uname=unamePwd.split(":")[0];
                String pwd=unamePwd.split(":")[1];
                /*cookie中的用户名密码正确才能自动登陆*/
                UserService service=BasicFactory.getFactory().getInstance(UserService.class);
                User user=service.login(uname,pwd);
                if(user!=null){
                    request.getSession().setAttribute("user",user);
                }
            }
        }
        chain.doFilter(request,response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
