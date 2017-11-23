package test3;

import org.apache.commons.dbutils.ProxyFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by tarena on 2016/9/13.
 */
public class BeanFactory {
    private BeanFactory(){};
    private static BeanFactory factory=new BeanFactory();
    public static BeanFactory getBeanFactory(){
        return factory;
    }
    private static Properties prop=new Properties();
        static{
            try {
//                String path=ProxyTest.class.getClassLoader().getResource("config.properties").getPath();
                prop.load(ProxyFactory.class.getClassLoader().getResourceAsStream("config.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    public Object getBean(String name){
        String className=prop.getProperty(name);//根据接口名配置相应的目标类名
        Object bean=null;//创建目标类对象
            try {
                Class clazz = Class.forName(className);
                bean=clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        if(bean instanceof ProxyFactoryBean){//判断是目标还是代理
            Object proxy=null;
            ProxyFactoryBean proxyFactoryBean=(ProxyFactoryBean)bean;
            try {
                Advice advice= (Advice) Class.forName(prop.getProperty(name+".advice")).newInstance();
                Object target=Class.forName(prop.getProperty(name+".advice")).newInstance();
                proxyFactoryBean.setAdvice(advice);
                proxyFactoryBean.setTarget(target);
                proxy =proxyFactoryBean.getProxy();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return proxy;//如果是代理bean就返回代理类对象
        }
        return bean; //不是代理就返回目标对象
     }
}
