package September_03;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;

/**
 * Created by gao on 2016/9/3.
 */
public class EncodingRequest extends HttpServletRequestWrapper {
    private HttpServletRequest request;
    public EncodingRequest(HttpServletRequest request) {
        super(request);
        this.request=request;
    }
    public String getParameter(String name){
        String value=request.getParameter(name);
        try {
            value=new String(value.getBytes("iso-8859-1"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }
}
