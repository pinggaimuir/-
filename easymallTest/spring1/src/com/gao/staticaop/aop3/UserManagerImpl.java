package com.gao.staticaop.aop3;

import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/18.
 */
//@Component("userManager")
@Component
public class UserManagerImpl implements  UserManager {
    private String name;
    @Override
    public void addUser(String useranme, String passwod) {
        System.out.println("---------------UserManagerImpl.add()------------------");
    }

    @Override
    public void delUser(int userId) {
        System.out.println("---------------UserManagerImpl.del()------------------");
    }

    @Override
    public String findUserBuyId(int userId) {
        System.out.println("---------------UserManagerImpl.findUserById()------------------");
        return "张三";
    }

    @Override
    public void modifyUser(int userId, String username, String password) {
        System.out.println("---------------UserManagerImpl.modifyUser()------------------");
    }
    private void checkSecurity(){
        System.out.println("-----------checkSecurity--------------");
    }
}
