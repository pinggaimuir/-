package aoptest;

import com.gao.staticaop.aop1.*;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by tarena on 2016/9/21.(
 */
public class AopTest extends TestCase {
    ApplicationContext context;
    protected void setUp() throws Exception {
        context=new ClassPathXmlApplicationContext("com/gao/staticaop/applicationContext-aop2.xml");
    }
    public void testUser(){
        context.getBean("userService", UserService.class).add();
    }

    public void testProxyFactory(){
        txTransaction tx=context.getBean("tx",txTransaction.class);
        UserService us=context.getBean("userServiceImpl",UserService.class);
        UserService service=   context.getBean("pfb", ProxyFactoryBean.class).getProxy(us,tx);
        System.out.println(service.getClass().getName());
    }
    /*测试用cglib来创建代理对象（继承目标类）*/
    public void testCglib(){
        txTransaction tx=context.getBean("tx",txTransaction.class);
        Product p=context.getBean("product",Product.class);
        Product service=   context.getBean("cglib", CglibProxy.class).getProxy(p,tx);
        System.out.println(service.getClass().getName());
    }

    public void testAspect(){
        Product prod=context.getBean("product",Product.class);
        System.out.println(prod.getClass().getName());
        prod.add();
    }
    public void testIntrospector(){
        context.getBean("introspector1",Introspector1.class).getBenaIfo(User.class);
    }
}
