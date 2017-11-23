package test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gao on 2017/3/1.
 */
public class Test2 {
    public static void main(String[] args) {
//        new Test();
        Map map=new HashMap<>();
    }
}
class Apple{
    Apple(String s){
        System.out.println(s);
    }
    Apple(){
        System.out.println("App默认构造函数被调用");
    }
}
class Test{
    static Apple app=new Apple("静态成员app初始化");
    Apple app1=new Apple("app1成员初始化");
    static{
        System.out.println("static块执行");
        if(app==null)
            System.out.println("app is null");
        app=new Apple("静态块内初始化app成员变量");

    }
    Test(){
        System.out.println("Test默认构造函数被调用");
    }
}
