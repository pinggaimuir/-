package com.gao.staticaop.aop1;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**用cglib来创建代理对象（继承目标类）
 * Created by tarena on 2016/9/21.
 */
//@Component("cglib")
public class CglibProxy {
   public static <T>T getProxy(final T target,final txTransaction tx){
       //增强器
       Enhancer enhancer=new Enhancer();
       //设置参数
       enhancer.setInterfaces(target.getClass().getInterfaces());
       //设置目标对象类为父类
       enhancer.setSuperclass(target.getClass());
       enhancer.setCallback(new MethodInterceptor() {
           @Override
           public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
               try {
                   tx.startTransaction();
                   Object result = method.invoke(target, objects);
                   tx.commitTransaction();
                   return result;
               }catch(Exception e){
//                   tx.rollbackTransaction();
                   throw  new RuntimeException(e);
               }
           }
       });
    return (T) enhancer.create();
   }
}
