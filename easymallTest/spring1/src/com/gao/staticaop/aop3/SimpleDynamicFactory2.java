package com.gao.staticaop.aop3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by tarena on 2016/9/20.
 */
public class SimpleDynamicFactory2 {
    private  SimpleDynamicFactory2(){}
    private static  SimpleDynamicFactory2 factory=new SimpleDynamicFactory2();
    public static SimpleDynamicFactory2 getFactory(){
        return factory;
    }
    public static <T>T getInstance(Class<T> clazz){

        try {
           final T t= (T) Class.forName(clazz.getSimpleName()).newInstance();
            if(Service1.class.isAssignableFrom(clazz)){
               T proxy=(T) Proxy.newProxyInstance(t.getClass().getClassLoader(),
                                                                            t.getClass().getInterfaces(),
                                                                            new  InvocationHandler(){
                     public Object invoke(Object proxy, Method method, Object[] params){
                         if(method.isAnnotationPresent(Tran1.class)){
                             System.out.println("开启事物！");
                             try {
                                Object result= method.invoke(t,params);
                                 System.out.println("提交事物！");
                                 return result;
                             } catch (IllegalAccessException e) {
                                 System.out.println("回滚事物！");
                                 e.printStackTrace();
                                 throw new RuntimeException(e);
                             } catch (InvocationTargetException e) {
                                 e.printStackTrace();
                                 throw new RuntimeException(e);
                             }
                         }else{
                             try {
                                 return method.invoke(t,params);
                             } catch (IllegalAccessException e) {
                                 e.printStackTrace();
                                 throw  new RuntimeException(e);
                             } catch (InvocationTargetException e) {
                                 e.printStackTrace();
                                 throw new RuntimeException(e);
                             }
                         }
                     }
                                                                            });
                return proxy;
            }else{
                return t;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
 }
