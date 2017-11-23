package com.gao.staticaop.aop1;

import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/21.
 */
@Component
public class Product {
    public void add(){
        System.out.println("prod");
    }
}
