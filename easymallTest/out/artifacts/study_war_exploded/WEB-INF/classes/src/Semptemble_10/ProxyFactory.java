package Semptemble_10;

import cn.tedu.service.UserService;
import cn.tedu.utils.PropUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by tarena on 2016/9/18.
 */
public class  ProxyFactory {
    private ProxyFactory(){}
    private static ProxyFactory factory=new ProxyFactory();
    public static <T>T getInstance(Class<T> targetInter){
        String targetName=PropUtils.getProp(targetInter.getSimpleName());
        try {
            Class target=Class.forName(targetName);
           final T t=(T)target.newInstance();
                 if(UserService.class.isAssignableFrom(targetInter) ){
                     T proxy =(T) Proxy.newProxyInstance(t.getClass().getClassLoader(),
                                                                                        t.getClass().getInterfaces(),
                                                                                        new InvocationHandler() {
                                public Object invoke(Object proxy, Method method, Object[] params) {
                                    if(method.isAnnotationPresent(Tran.class)){
                                        //preAdvice
                                        try {
                                           Object obj= method.invoke(t,params);
                                            //afterAdvice
                                            return obj;
                                        } catch (IllegalAccessException e) {
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
                                            throw new RuntimeException(e);
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
