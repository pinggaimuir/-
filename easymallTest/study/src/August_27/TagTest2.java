package August_27;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;

/**
 * Created by tarena on 2016/9/3.
 */
public class TagTest2 extends SimpleTagSupport {
    private String decode="utf-8";
    public void setDecode(String decode){
        this.decode=decode;
    }
    public void doTaq() throws IOException, JspException {
        JspFragment fragment=this.getJspBody();
        StringWriter writer=new StringWriter();
        fragment.invoke(writer);
        String str=writer.toString();
         str= URLDecoder.decode(str,decode);
        this.getJspContext().getOut().write(str);
    }

}
