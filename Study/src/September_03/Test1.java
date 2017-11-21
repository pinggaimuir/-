package September_03;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by gao on 2016/9/20.
 */
public class Test1 {

    public static void testSet(){
        Class clazz= null;
        try {
            clazz = Class.forName("September_03.User1");
            Object user1=clazz.newInstance();
            Method setName=clazz.getMethod("setName",String.class);
            setName.invoke(user1,"gao");
            Method getNmae=clazz.getMethod("getName");
            System.out.println(getNmae.invoke(user1));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        testSet();

    }
}
