package com.gao.practice9_23;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by tarena on 2016/9/23.
 */
@Aspect
@Component
public class AspectTransaction {
    /*监控执行时间*/
    /*事物切面*/
//    @Around("execution(* com.gao.practice9_23.servlet.*.*(..))&&execution(* com.gao.practice9_23.service.*.*(..))")
    @Around("execution(* com.gao.practice9_23.service.*.*(..))")
    public <T>T TransactionAspect(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        long start=System.currentTimeMillis();//方法执行开始时间
            Object target =joinPoint.getTarget();
             String methodName=joinPoint.getSignature().getName();
        System.out.println(joinPoint.getSignature());
            Object[] args=joinPoint.getArgs();
            Class[] cls=new Class[args.length];
            for(int i=0;i<args.length;i++){
                cls[i]=args[i].getClass();
            }
            Method method=target.getClass().getMethod(methodName,cls);
        T t=null;
        if(method.isAnnotationPresent(Transaction.class)){
            System.out.println("开启事物--------------------------------------");
            try {
               t=(T) joinPoint.proceed(joinPoint.getArgs());
                System.out.println("提交事物----------------------------------------");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                System.out.println("回滚事物-----------------------------------------");
                throw new RuntimeException(throwable);
            }
        }else{
            try {
               t=(T) joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        long end=System.currentTimeMillis();//方法执行结束时间
        System.out.println(joinPoint.getSignature().toShortString()+"执行时间:"+(end-start)+"毫秒");
        return t;
    }

    /*异常处理*/
    @AfterThrowing(pointcut="execution(* com.gao.practice9_23..*(..))",throwing="thro")
    public void handleException(JoinPoint joinPoint, Throwable thro){
        System.out.println("抛出异常的方法："+joinPoint.getSignature().toShortString());
        System.out.println("目标类的类："+joinPoint.getTarget().getClass());
        System.out.println("抛出异常的类："+joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("异常信息："+thro.getMessage());
    }
//    @Resource(name="privilege")
//    private List<String> privi;

    /*权限控制*/
    @Around("execution(* com.gao.practice9_23.service.*.*(..))")
    public void privilegeControl(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target =joinPoint.getTarget();
        String methodName=joinPoint.getSignature().getName();
        Object[] params=joinPoint.getArgs();
        List<String> privi=PriviThreadLocal.getPriviList();
        Class[] cls=new Class[params.length];
        for(int i=0;i<params.length;i++){
            cls[i]=params[i].getClass();
        }
            /*获得目标方法*/
        Method method=target.getClass().getMethod(methodName,cls);
        if(method.isAnnotationPresent(PrivilegeInfo.class)){//判断方法上是否有注解
            Annotation ann=method.getAnnotation(PrivilegeInfo.class);//获得注解
           if(((PrivilegeInfo)ann).name().equals(methodName)){//判断注解和属性name值
               boolean flag=false;
                    for(String str:privi){
                        if(str.equals(((PrivilegeInfo)ann).name())){
                            joinPoint.proceed(joinPoint.getArgs());
                            flag=true;
                            break;
                        }
                    }
               if(!flag){
                   System.out.println("您的权限不足");
               }
            }
        }else{
            joinPoint.proceed(joinPoint.getArgs());
        }
    }



}
