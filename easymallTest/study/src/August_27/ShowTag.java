package August_27;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import java.io.IOException;

/**
 * Created by tarena on 2016/9/2.
 */
//1 在jsp页面云翔的过程中遇到自定义标签，首先创建对应的自定义对象，执行构造方法
public class ShowTag implements SimpleTag {

    PageContext pageContext;
    //2 在调用自定义标签对象的setJSPContext方法，将地昂钱的jsp页面的pageCOntext对象传入
    public void setJspContext(JspContext jspContext) {
        pageContext=(PageContext)jspContext;
    }

    //3如果有父标签则调用setParent方法将夫标签传入，如没有则不调用
    public void setParent(JspTag jspTag) {

    }

    //获取父标签
    public JspTag getParent() {
        return null;
    }


    //4如果当前标签有表前提，则调用setJspBody方法，将封装的表前提的内容信息的jspFragment类的对象
    //传入，如果没有表前提则不执行
    public void setJspBody(JspFragment jspFragment) {

    }
    //5如果具有属性吗，则调用属性的setXXX方法，想属性的值传入
    //6 最后调用doTag方法执行自定义标签
    public void doTag() throws JspException, IOException {
        String ipAddr=(String)pageContext.getRequest().getRemoteAddr();
        pageContext.getOut().write(ipAddr);
    }
}
