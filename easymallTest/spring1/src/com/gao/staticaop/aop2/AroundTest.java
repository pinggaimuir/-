package com.gao.staticaop.aop2;

import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/23.
 */
@Component("aroundTest")
public class AroundTest {
    public String say(){
        System.out.println("这里是环绕方法！");
        return "say环绕通知返回";
    }
}
