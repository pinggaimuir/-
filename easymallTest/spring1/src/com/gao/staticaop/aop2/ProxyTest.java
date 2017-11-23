package com.gao.staticaop.aop2;

import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/22.
 */
@Component("proxyTest")
public class ProxyTest implements ProxyTestInterface{
    public void say(){
        System.out.println("没实现接口的类");
    }
}
