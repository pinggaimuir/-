package test3;

/**
 * Created by tarena on 2016/9/13.
 */
public class ProxyTest {
    public static void main(String[] args) {
        Object bean=BeanFactory.getBeanFactory().getBean("xxx");
        System.out.println(bean.getClass().getName());
    }
}
