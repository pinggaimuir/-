package com.gao.staticaop.aop3;
/**
 * 简单的动态代理  代理一个类
 * Created by tarena on 2016/9/18.
 */
//
//public class SimpleDynamicProxy {
//    private SimpleDynamicProxy(){}
//    private static SimpleDynamicProxy dynamic=new SimpleDynamicProxy();
//    public static SimpleDynamicProxy getDynamic(){
//        return dynamic;
//    }
//    public static <T>T getProxy() {
//        try {
//           final T target =(T) Class.forName("com.gao.staticaop.UserManagerImpl").newInstance();
//            T proxy=(T)Proxy.newProxyInstance(target.getClass().getClassLoader(),
//                                                                    target.getClass().getInterfaces(),
//                                                                    new InvocationHandler(){
//                    public Object invoke(Object proxy,Method method,Object[] params) throws Throwable {
//                        try {
//                            System.out.println("kaishi le");
//                            Object obj= method.invoke(target,params);
//                            System.out.println("wanjiele ");
//                            return obj;
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                            throw  new RuntimeException(e);
//                        } catch (InvocationTargetException e) {
//                            e.printStackTrace();
//                            throw e.getTargetException();
//                        }
//                    }
//                                                                    });
//            return proxy;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//}
