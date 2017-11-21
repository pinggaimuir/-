package September_03;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by gao on 2016/9/3.
 */
@WebFilter(filterName = "StaticFilter")
public class StaticFilter implements Filter {
    private FilterConfig config;
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request=(HttpServletRequest)req;
        HttpServletResponse response=(HttpServletResponse)resp;
        String category=request.getParameter("category");
        String htmlPage=category+".html";
        String htmlPath=config.getServletContext().getRealPath("/htmls");
        File destPage=new File(htmlPath,htmlPage);
        if(destPage.exists()){
            response.sendRedirect(request.getContextPath()+"/htmls"+htmlPage);
            return;
        }

        StaticResponse sr=new StaticResponse(response,destPage.getAbsolutePath());
        chain.doFilter(req, sr);
        response.sendRedirect(request.getContextPath()+"/html"+htmlPage);
    }

    public void init(FilterConfig config) throws ServletException {
        this.config=config;
    }

}
