package beanstest;

import com.gao.beantest1.Bean1;
import com.gao.beantest1.Bean4;
import com.gao.beantest1.InitMethod;
import junit.framework.TestCase;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Calendar;

/**
 * Created by tarena on 2016/9/18.
 */

public class BeanTest  extends TestCase {
/*    private BeanFactory factory=null;
    public void setUp() throws Exception {
        String[] xmlArray=new String[]{"applicationContext-aop.xml","applicationContext-bean.xml"};
        factory =new ClassPathXmlApplicationContext(xmlArray);
    }*/
    private BeanFactory application=null;
    public void setUp() throws Exception {
        String[] xmlArray=new String[]{"applicationContext-aop.xml","applicationContext-bean.xml"};
        application =new ClassPathXmlApplicationContext(xmlArray);
    }

    public void tearDown() throws Exception {
//        application.close();
    }

    public void testBean1(){
        Bean1 bean1=(Bean1)application.getBean(Bean1.class);
        System.out.println(bean1);
        System.out.println((Bean1)application.getBean("bean1"));
/*        Bean3 bean3=(Bean3)application.getBean(Bean3.class);

        List list=bean3.getPrice();
        for(Object in:list) {
            System.out.println(in);
        }
        Bean4 bean4=(Bean4)application.getBean("bean4");*/
/*        UserManager u=(UserManager)application.getBean("UserManagerImpl");
        u.addUser("gao","jian");
        System.out.println("++++++++++++++++++++++++++++++++");
        u.findUserBuyId(52);
        System.out.println("++++++++++++++++++++++++++++++++");
    u.delUser(5);*/
    }
    /*创建静态工厂*/
    public void testCalendar(){
        Calendar calendar= (Calendar) application.getBean("StaticFactory");
        System.out.println( ("jingtai---------"+(Calendar) application.getBean("StaticFactory")));
        System.out.println("jingtai-----------"+calendar.getTime());
    }
    /*创建实例工厂*/
    /*创建实力工厂需要创建工厂的对象*/
    public void testNewInstance(){
        Calendar calendar= (Calendar) application.getBean("newCalendar");
        Calendar calendar2= (Calendar) application.getBean("newCalendar");
        System.out.println("shili"+calendar);
        System.out.println("shili"+calendar2);
    }
    /*spring工厂*/
//    public void testSpringFactory(){
//        Calendar calendar= (Calendar) application.getBean("SpringFactory");
//        System.out.println(calendar);
//    }
    /*初始化方法*/
    public void testInItMethod(){
        InitMethod initMethod= (InitMethod) application.getBean("InitMethod");
    }
    /*销毁方法*/
    public void testDestory(){
        InitMethod initMethod= (InitMethod) application.getBean("InitMethod");
    }
    public void testSpringFactory2(){
        Bean1 bean1=application.getBean("SpringFactory2",Bean1.class);
        System.out.println(bean1.getDate());
    }
    public void testBean11(){
        Bean4 bean44=application.getBean("bean4",Bean4.class);
        System.out.println(bean44.getBean1().getProp());
    }
}
