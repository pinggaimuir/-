package September_03;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by gao on 2016/9/3.
 */
public class StaticResponse extends HttpServletResponseWrapper {
    //private HttpServletResponse response;
    private PrintWriter pw;
    public StaticResponse(HttpServletResponse response,String path) throws FileNotFoundException, UnsupportedEncodingException {
        super(response);
        //this.response=response;
        pw=new PrintWriter(path,"utf-8");
    }
    public PrintWriter getWriter(){
        return pw;
    }
}
