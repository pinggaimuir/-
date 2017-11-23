package com.gao.staticaop.aop2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/23.
 */
@Component
@Aspect
public class AspectTest2 {
    @Around("bean(aroundTest)")
    public void AroundTest2(ProceedingJoinPoint joinPoint){
        System.out.println("环绕前置通知3");
        Object o=null;
        try {
           o= joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("环绕后置通知3");
//        return o;
    }
}
