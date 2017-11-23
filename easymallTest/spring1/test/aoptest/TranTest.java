package aoptest;

import com.gao.practice9_23.Exception.XXXException;
import com.gao.practice9_23.PriviThreadLocal;
import com.gao.practice9_23.servlet.PersonServlet;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 4个练习切面的测试
 * Created by tarena on 2016/9/23.
 */
public class TranTest extends TestCase {
    /*初始化*/
    private ApplicationContext context;
    protected void setUp() throws Exception {
        context = new ClassPathXmlApplicationContext("com/gao/practice9_23/applicationContext-practice.xml");
    }

    public void testTan() throws XXXException {
        context.getBean("personServlet", PersonServlet.class).save();
    }

    public void testPrivilegeInfo(){
        List<String> privi=new ArrayList();
        privi.add("delPerson");
        privi.add("find");
        PriviThreadLocal.setPriviList(privi);
        context.getBean("personServlet",PersonServlet.class).find("100");
    }
}
