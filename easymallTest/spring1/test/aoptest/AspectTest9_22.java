package aoptest;

import com.gao.staticaop.aop2.AroundTest;
import com.gao.staticaop.aop2.ProdAddedInterface;
import com.gao.staticaop.aop2.ProxyTestInterface;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by tarena on 2016/9/22.
 */
public class AspectTest9_22 extends TestCase {
    ApplicationContext context=null;
    protected void setUp() throws Exception {
        context=new ClassPathXmlApplicationContext("com/gao/staticaop/applicationContext-aop2.xml");
    }
    /*在没有开启CGlib动态代理的情况下，实现了接口的类回强制使用JDK动态代理，如果没有用接口声明得到的对象，则回报错*/
    public void testProdAdd(){
        ProdAddedInterface prod= (ProdAddedInterface) context.getBean("prodAddedInterface");
        System.out.println(prod.getClass().getName());
        System.out.println(prod.ProdAdded1("gaojian"));
    }
    public void testJDKDynamicProxy(){
        ProxyTestInterface pt=context.getBean("proxyTest", ProxyTestInterface.class);
        System.out.println(pt.getClass());
        pt.say();
    }
    public void testAroundTest(){
        AroundTest at=context.getBean("aroundTest", AroundTest.class);
        System.out.println(at.say());
    }
}
