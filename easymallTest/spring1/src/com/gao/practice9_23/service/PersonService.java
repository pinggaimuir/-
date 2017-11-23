package com.gao.practice9_23.service;

import com.gao.practice9_23.Transaction;

/**
 * Created by tarena on 2016/9/23.
 */

public interface PersonService {
    @Transaction
    void addUser();

    void delPerson();

    void find(String str);
}
