package beanstest;

import com.gao.staticaop.aop3.UserManager;
import com.gao.staticaop.aop3.UserManagerImpl;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by tarena on 2016/9/18.
 */
public class SDProxyTest extends TestCase {
    public void test(){
  /*      UserManager userManager= SimpleDynamicProxy.getDynamic().getProxy();
//        System.out.println(o.getClass());
        userManager.addUser("gao","jian");*/
        UserManagerImpl userManager=new UserManagerImpl();
        userManager.addUser("gaojian","yue");
    }

    private ApplicationContext context=null;
    protected void setUp() throws Exception {
        context=new ClassPathXmlApplicationContext("applicationContext-aop.xml");
    }

    public void testAddMethod(){
        UserManager userManager= context.getBean("userManagerImpl",UserManager.class);
        userManager.addUser("gao","jian");
    }
}
