package test3;

import java.lang.reflect.*;
import java.util.List;

/**
 * Created by tarena on 2016/9/12.
 */
public class Proxy1 {


    public static void main(String[] args) {
        Class Proxy2= Proxy.getProxyClass(List.class.getClassLoader(), List.class);
        Method[] constructors=Proxy2.getMethods();
        try {
            Constructor constructor=Proxy2.getConstructor();
            List aaa=(List)constructor.newInstance();
            System.out.println(aaa);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    /*    for(Method constructor:constructors) {
            System.out.println(constructor.getName());
            Type[] types= constructor.getParameterTypes();
            for(Type type:types){
                System.out.println(type.toString());
            }
            System.out.println("-------------------------------------------------------------------------");
        }*/
//        Proxy2 proxy=new Proxy2( new InvocationHandler(){
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                return null;
//            }
//        });
//        Class proxy2=Proxy.newProxyInstance(UserService.class.getClassLoader(),UserService.class,new MyInvocationHandler());

    }


}
class MyInvocationHandler implements InvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args) {
        return null;
    }

}
