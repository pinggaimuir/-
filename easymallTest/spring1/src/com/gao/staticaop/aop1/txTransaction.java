package com.gao.staticaop.aop1;

import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/21.
 */
//@Aspect
@Component("tx")
public class txTransaction {
//    @Before("execution(* com.gao.staticaop.aop1.UserServiceImpl.add*(..))")
    public void startTransaction(){
        System.out.println("kaiqishiwu--------------------------");
    }
//    @AfterReturning("execution(* com.gao.staticaop.aop1.UserServiceImpl.del*(..))")
    public void commitTransaction(){
        System.out.println("tijiaoshiwu ------------------------");

    }
//    @Around("within( com.gao.staticaop.aop1.UserServiceImpl)")
    public void rollbackTransaction(){
        System.out.println("huigunshiwu------------------------");

    }
}
