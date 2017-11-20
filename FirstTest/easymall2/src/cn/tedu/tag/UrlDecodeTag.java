package cn.tedu.tag;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by tarena on 2016/9/2.
 */
public class UrlDecodeTag extends SimpleTagSupport {
/*    private String encode="utf-8";
    public void setEncode(String encode){
        this.encode=encode;
    }

    public void doTag()throws JspException,IOException {
        JspFragment fragment=this.getJspBody();
        StringWriter writer=new StringWriter();
        fragment.invoke(writer);
        String str=writer.toString();
        str= URLDecoder.decode(str,encode);
        getJspContext().getOut().write(str);

    }*/
    private String name="";

    public void setName(String name) {
        this.name = name;
    }
    public void doTag()throws IOException,JspException {
        PageContext pc= (PageContext) this.getJspContext();
        Cookie cs[]=((HttpServletRequest)pc.getRequest()).getCookies();
        Cookie rename=null;
        if(cs!=null){
            for(Cookie cookie:cs){
                if(name.equals(cookie.getName())){
                    rename=cookie;
                    break;
                }
            }
        }
        String username="";
        if(rename!=null){
            username= URLDecoder.decode(rename.getValue(),"utf-8");
        }
        this.getJspContext().getOut().write(username);
    }
}
