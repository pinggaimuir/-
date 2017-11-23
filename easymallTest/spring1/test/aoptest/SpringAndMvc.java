package aoptest;

import com.gao.springjdbc.domain.User3;
import com.gao.springjdbc.servlet;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

/**
 * Created by tarena on 2016/9/24.
 */
public class SpringAndMvc extends TestCase {
    private ApplicationContext context=null;
    protected void setUp() throws Exception {
        context=new ClassPathXmlApplicationContext("com/gao/springjdbc/applicationContext-jdbc.xml");
    }

    public void test01(){
        User3 user=context.getBean("servlet", servlet.class).selectOne(2);
        System.out.println(user.toString());
    }
    public void test02()throws SQLException{
        User3 user=new User3();
//        user.setId(4);
        user.setName("天狼2号");
        user.setSalary(10234);
        user.setSex("男");
        context.getBean("servlet", servlet.class).addUser(user);

    }
}
