package test2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by tarena on 2016/9/6.
 */
public class MySCListener  implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent){
        System.out.println("服务器启动了");
    }
    public void contextDestroyed(ServletContextEvent servletContextEvent){
        System.out.println("服务器要停滞了");
    }


}
