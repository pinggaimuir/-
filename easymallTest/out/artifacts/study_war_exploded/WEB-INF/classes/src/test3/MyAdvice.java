package test3;

/**
 * Created by tarena on 2016/9/13.
 */
public class MyAdvice implements Advice {
    public void beforeMethod(){
        System.out.println("方法要开始了");
    }
    public void afterMethod(){
        System.out.println("方法要结束了");
    }
}
