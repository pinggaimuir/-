package com.gao.practice9_23.service;

import com.gao.practice9_23.PrivilegeInfo;
import com.gao.practice9_23.Transaction;
import com.gao.practice9_23.dao.PersonDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/9/23.
 */
@Component("personService")
public class PersonServiceImpl implements PersonService {
    @Resource(name="personDao")
    private PersonDao personDao;

    @Transaction
    public void addUser(){
        personDao.addUser();
    }
    @Transaction
    @PrivilegeInfo(name="delPerson")
    public void delPerson() {
        personDao.delPerson();
    }
    @Transaction
    @PrivilegeInfo(name="find")
    public void find(String str) {
            personDao.find(str);
    }
}
