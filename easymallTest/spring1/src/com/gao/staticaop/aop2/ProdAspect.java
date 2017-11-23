package com.gao.staticaop.aop2;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/22.
 */
@Component
@Aspect
public class ProdAspect {
    @Before(value = "execution(* com.gao.staticaop.aop2.*.ProdAdded1(String))&&args(str))")
    public void ProdBefore(JoinPoint point,String str){
        System.out.println("这是前置问候"+"-----"+str);
    }
    @AfterThrowing(pointcut="execution(* com.gao.staticaop.aop2.*.ProdAdded1(String))",throwing = "thro")
    public void ProdThrow(Throwable thro){
        System.out.println("这是异常通知");
    }
    @AfterReturning(pointcut="execution(String com.gao.staticaop.aop2.AroundTest.*(..))",returning="retVal")
    public String ProdReturning(JoinPoint jointPoint,String retVal){
        System.out.println("这是后置通知"+"---------"+retVal);
        return retVal;
    }
    @Before("bean(proxyTest)")
    public void ProxtTestBefore(){
        System.out.println("JDK动态代理的前置通知");
    }

/*    @Around("bean(aroundTest)")
    public Object AroundTest2(ProceedingJoinPoint joinPoint){
        System.out.println("环绕前置通知2");
        Object o=null;
        try {
            o=joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("环绕后置通知2");
        return o;
    }

    @Around("bean(aroundTest)")
    public void AroundTest1(ProceedingJoinPoint joinPoint){
        System.out.println("环绕通知前置");
        Object o=null;
        try {
            o= joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("环绕通知异常");
        }
        System.out.println("环绕通知后置");
//        return o;
    }*/
}
