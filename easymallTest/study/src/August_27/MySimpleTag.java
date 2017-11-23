package August_27;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by tarena on 2016/9/2.
 */
public class MySimpleTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException, IOException {
        //1. 标签体内的内容是否输出
        //  控制标签体不输出，什么都不做，默认不输出
        JspFragment fragment=getJspBody();
        //fragment.invoke(getJspContext().getOut());
        fragment.invoke(null);
        //2 控制标签体后的内容是否输出，默认为输出
     //   throw new SkipPageException();//设置为不输出
        //3  重复执行标签体
        for(int i=0;i<3;i++){
            getJspBody().invoke(null);
        }
        //4 修改标签体后输出
        StringWriter writer=new StringWriter();
        JspFragment fragment1=getJspBody();
        fragment.invoke(writer);
        //chuli
        String str=writer.toString();
        str=str.toLowerCase();
        //输出
        getJspContext().getOut().write(str);

        //5 开发带属性的标签
        //设置循环多少次，修改类，修改tld
        /*在tag方法外定义属性，并且写出属性的get方法*/
    }
}
