package com.gao.beantest1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/18.
 */
//@Repository("")
    @Component("p")
    @Scope("singleton")
    @Lazy
public class Bean2 {
    public Bean2(){

    }
    @Value("wukong")
    private String name;
    @Value("wuneng")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
