package mvc;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.Controller;

import java.util.Properties;

/**
 * Created by tarena on 2016/9/21.
 */
public class TestHandleMapping extends TestCase {
    ApplicationContext context=null;
    protected void setUp() throws Exception {
        context=new ClassPathXmlApplicationContext("com/gao/mvc1/simplehandle.xml");
    }
    public void testMapping(){
        SimpleUrlHandlerMapping obj= (SimpleUrlHandlerMapping) context.getBean("handlerMapping");
        Properties prop=context.getBean("urlMappings",Properties.class);
        Controller con=context.getBean("helloRequest",Controller.class);
        System.out.println(obj);
        System.out.println(prop);
        System.out.println(con);
    }
}
