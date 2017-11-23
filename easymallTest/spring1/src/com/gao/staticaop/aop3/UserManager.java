package com.gao.staticaop.aop3;

/**
 * Created by tarena on 2016/9/18.
 */

public interface UserManager {
    public void addUser(String useranme,String passwod);
    public void delUser(int userId);
     String findUserBuyId(int userId);
    void  modifyUser(int userId,String username,String password);
}
