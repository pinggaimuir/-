package com.gao.staticaop.aop1;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/9/21.
 */
@Aspect
@Component
public class TxAspect {
    @Resource(name="tx")
    private txTransaction tx;
    @Around("execution(* com.gao.staticaop.aop1.Product.*(..))")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
            tx.startTransaction();
            joinPoint.proceed();
            int i=1/0;
            tx.commitTransaction();
        System.out.println( joinPoint.getSignature().getName());
    }
    @After("execution(* com.gao.staticaop.aop1.Product.*(..))")
    public void after(JoinPoint joinPoint){
        System.out.println(joinPoint.getArgs().getClass());
        System.out.println(joinPoint.getThis());
        System.out.println("后置通知");
    }
    @AfterReturning("execution(* com.gao.staticaop.aop1.Product.*(..))")
    public void AfterReturn(){
        System.out.println("正常执行");
    }
    @Before("within(com.gao.staticaop.aop1.Product)")
    public void before(){
        System.out.println("前置通知");
    }
//    @AfterThrowing("within(com.gao.staticaop.aop1.Product)")
    public void afterThrow(JoinPoint joinPoint,Throwable thro){
        System.out.println(thro.getMessage());
        System.out.println("异常通知");
    }
}
