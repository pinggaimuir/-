package test2;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;

/**
 * Created by tarena on 2016/9/6.
 */
public class MyHttpSession implements ServletRequestAttributeListener
{


    @Override
    public void attributeAdded(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        String name=servletRequestAttributeEvent.getName();
        Object value = servletRequestAttributeEvent.getValue();
        System.out.println("添加"+name+"--------------"+value);
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        String name=servletRequestAttributeEvent.getName();
        Object value = servletRequestAttributeEvent.getValue();
        System.out.println("要死了：：：："+name+"--------------"+value+"------"+servletRequestAttributeEvent.getServletRequest().getAttribute("gao"));
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent servletRequestAttributeEvent) {
        String name=servletRequestAttributeEvent.getName();
        Object value = servletRequestAttributeEvent.getValue();
        System.out.println("替换"+name+"--------------"+value+"------"+servletRequestAttributeEvent.getServletRequest().getAttribute("gao"));
    }
}
