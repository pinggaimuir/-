package August_27;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarena on 2016/9/3.
 */
class Person1{

    public void say1(){
        System.out.println("person1");
    }
}
class  Person2 extends Person1{
    public void say2(){
        System.out.println("person2");
    }
}
class Person3 extends Person2{
    public void say3(){
        System.out.println("person3");
    }
}
public class Dog<T extends Exception> {
    public static void main(String[] args) {
        List<? super Person2> l2=new ArrayList<Person1>();
        l2.add(new Person3());
        List<? extends Person2> list2=new ArrayList<Person3>();

    }
    // 1.当作形参
    public void print1(List<? extends Person1> list){
        List<Person2> p2=new ArrayList<Person2>();
        p2.add(new Person2());
        p2.add(new Person3());
    }
    //2.
    public void method2(T t){
        t.printStackTrace();
        Dog<RuntimeException> e1=new Dog<RuntimeException>();
    }
}
