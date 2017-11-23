package com.gao.staticaop.aop1;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/9/21.
 */
@Component("userService")
public class UserServiceImpl implements UserService {
    @Resource(name="userDao")
    private UserDao userDao;


    @Override
    public void add() {
        userDao.addUser();
    }
    public void del(){
        userDao.delUser();
    }







}
