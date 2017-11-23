package com.gao.staticaop.aop1;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * Created by tarena on 2016/9/21.
 */
//@Component("pfb")
public class ProxyFactoryBean {
    public static <T>T getProxy(final T target,final txTransaction tx){
//    public static Object getProxy(final UserService target,final txTransaction tx){
        T proxy= (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                                                                        target.getClass().getInterfaces(),
                                                                        new InvocationHandler(){
                 public Object invoke(Object proxy, Method method, Object[] params){
                     tx.startTransaction();
                     try {
                         Object result=method.invoke(target,params);
                         tx.commitTransaction();
                         return result;
                     } catch (IllegalAccessException e) {
                         e.printStackTrace();
//                         tx.rollbackTransaction();
                         throw new RuntimeException(e);
                     } catch (InvocationTargetException e) {
                         e.printStackTrace();
//                         tx.rollbackTransaction();
                         throw new RuntimeException(e);
                     }


                 }
                                                                        });
        return proxy;
    }
}
