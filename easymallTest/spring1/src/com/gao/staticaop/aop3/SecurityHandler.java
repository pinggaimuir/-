package com.gao.staticaop.aop3;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 用注解的方式使用aop产生动态代理
 * Created by tarena on 2016/9/18.
 */
//@Component
//@Aspect
public class SecurityHandler {
    //该方法用于表示那些类为要插入advice的类，并不具有实用功能
//    @Pointcut("execution(* add*(..))")
//    public void mark(){}
//    @After("mark()")
    @Pointcut("execution(* add*(..))")
    public void mark1(){}

    @Before("mark1()")
    @After("mark1()")
    private void checkBerore(){
        System.out.println("---------------------------Before-------------------------");
    }
//    @Before("execution(* add*(..))")
//    @After("execution(* add*(..))")
//    private void checkSecurity(){
//        System.out.println("--------------------checkSecurity--------------------");
//    }
}
