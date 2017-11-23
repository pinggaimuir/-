package beanstest;

import com.gao.beantest1.Bean1;
import com.gao.beantest1.Message;
import com.gao.beantest1.PropService;
import com.gao.beantest1.UserDao;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring注入测试
 * Created by tarena on 2016/9/20.
 */
public class InjectTest extends TestCase {
    ApplicationContext context=null;
    protected void setUp() throws Exception {
        String[] xmls=new String[]{"applicationContext-bean.xml"};
        context=new ClassPathXmlApplicationContext(xmls);
    }
    public void testBean1(){
        Bean1 bean1= (Bean1) context.getBean("bean13");
        System.out.println(bean1.getUsername());
        System.out.println(bean1.getProp());
        System.out.println(bean1.getPassword());
    }
    public void testUserDao(){
        UserDao userDao=context.getBean("userDao",UserDao.class);
        System.out.println( userDao.getBean2().getPassword());
    }
    public void testUserDao1(){
        UserDao userDao=context.getBean("userDao1",UserDao.class);
        System.out.println( userDao.getBean1().getDate());
    }
    public void testMessage(){
        Message message=context.getBean("message",Message.class);
        System.out.println(message.toString());
    }
    public void testPropService(){
        PropService service=context.getBean("propService",PropService.class);
        service.printUrl();
    }
    public void test2(){
        UserDao userDao=context.getBean("userDao1",UserDao.class);
        System.out.println( userDao.getName());
    }
}
