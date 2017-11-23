package com.gao.springjdbc.dao;

import com.gao.springjdbc.domain.User3;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tarena on 2016/9/24.
 */
public interface UserDao2 {
    void addUser(User3 user) throws SQLException;

    int delUser(int id);

    int update(User3 user);

    User3 findOne(int id);

    List<User3> findList();
}
