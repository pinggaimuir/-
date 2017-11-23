package com.gao.practice9_23.dao;

import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/23.
 */
@Component("personDao")
public class PersonDaoImpl implements PersonDao {

    public void addUser() {
        System.out.println("成功的添加用户- 。-");
    }


    public void delPerson() {
        System.out.println("删除用户成功！");
    }


    public void find(String str) {
        System.out.println("查找用户！"+str);
    }
}
