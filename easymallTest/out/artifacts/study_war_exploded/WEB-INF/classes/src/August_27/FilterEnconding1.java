package August_27;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by tarena on 2016/8/27.
 */
public class FilterEnconding1 implements Filter {
    String encoding;
    public void init(FilterConfig config) throws ServletException {
        System.out.println("初始化---------------");
        encoding=config.getInitParameter("Encoding");
        if(encoding==null){
            throw new ServletException("config中的编码为设置为空！");
        }
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("dofilter前-------------");
        if(!encoding.equals(req.getCharacterEncoding())){
            req.setCharacterEncoding(encoding);
        }
        resp.setCharacterEncoding(encoding);
       resp.setContentType("text/html;charset=utf-8");
        chain.doFilter(req, resp);
        System.out.println("doFilter后------------");
    }

    public void destroy() {
        System.out.println("销毁-----------------");
    }

}
