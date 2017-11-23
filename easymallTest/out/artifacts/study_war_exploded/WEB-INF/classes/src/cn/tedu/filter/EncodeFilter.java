package cn.tedu.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 全站编码转换
 * Created by tarena on 2016/9/4.
 */
public class EncodeFilter implements Filter {
    private FilterConfig config = null;
    private String encode = null;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        EncodeRequest er = new EncodeRequest(request);
        resp.setContentType("text/html;charset=utf-8");
        chain.doFilter(er, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
//        this.config.getServletContext().getInitParameter(encode);
    }

     class EncodeRequest extends HttpServletRequestWrapper {
        private HttpServletRequest request = null;
        private boolean hasNotEncode = true;

        public EncodeRequest(HttpServletRequest request) {
            super(request);
            this.request = request;
        }

         /**
          * 获得参数键值对的Map
          * @return
          */
        public Map<String, String[]> getParameterMap() {
            try {
                if ("POST".equals(request.getMethod())) {
                    request.setCharacterEncoding("utf-8");
                    return request.getParameterMap();
                } else if ("GET".equals(request.getMethod())) {
                    Map<String, String[]> map = request.getParameterMap();
                    if (hasNotEncode) {
                        for (Map.Entry<String, String[]> entry : map.entrySet()) {
                            String vs[] = entry.getValue();
                            for (int i = 0; i < vs.length; i++) {
                                vs[i] = new String(vs[i].getBytes("iso-8859-1"), "utf-8");
                            }
                        }
                        hasNotEncode = false;
                    }
                    return map;
                } else {
                    return request.getParameterMap();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

         /**
          * 获得参数值数组
          * @param name
          * @return
          */
        public String[] getParameterValues(String name) {
            return getParameterMap().get(name);
        }

         /**
          * 获得单个参数值
          * @param name
          * @return
          */
        public String getParameter(String name) {
            String[] vs = getParameterValues(name);
            return vs == null ? null : vs[0];
        }
    }
}
