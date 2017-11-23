package com.gao.staticaop.aop2;

import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/22.
 */

@Component("prodAddedInterface")
public class ProdAdded implements ProdAddedInterface {
    public String ProdAdded1(String str111111){
        System.out.println(str111111+"-----------------------我时那个苦逼的目标对象！！！！！！！！！！");
        return "太行山";
    }
}
