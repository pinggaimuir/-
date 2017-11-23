package cn.tedu.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/9/5.
 */
@WebFilter(filterName = "NoCacheFilter")
public class NoCacheFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response=(HttpServletResponse)resp;
        response.setDateHeader("Expires",-1);
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Pragma","no-cache");

        chain.doFilter(req, response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
