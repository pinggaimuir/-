package September_03;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by gao on 2016/9/3.
 */
@WebFilter(filterName = "AFilter",urlPatterns={"/*"})
public class AFilter implements Filter {
    private FilterConfig config=null;
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request=(HttpServletRequest)req;
        if(request.getMethod().equals("GET")) {
            EncodingRequest er=new EncodingRequest(request);
            chain.doFilter(er, resp);

        }else if(request.getMethod().equals("POST")){
            req.setCharacterEncoding("utf-8");
            chain.doFilter(req,resp);
        }

    }

    public void init(FilterConfig config) throws ServletException {
        this.config=config;
    }

}
