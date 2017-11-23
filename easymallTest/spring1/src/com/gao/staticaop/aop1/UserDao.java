package com.gao.staticaop.aop1;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/9/21.
 */
@Component("userDao")
public class UserDao {
    @Resource(name="user")
    private User user;
    void addUser(){
        System.out.println(user.toString());
    }
    void delUser(){
        System.out.println("----------------------删除用户");
    }
}
