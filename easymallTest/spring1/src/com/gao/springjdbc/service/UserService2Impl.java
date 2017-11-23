package com.gao.springjdbc.service;

import com.gao.springjdbc.dao.UserDao2;
import com.gao.springjdbc.domain.User3;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by tarena on 2016/9/24.
 */
@Service
public class UserService2Impl implements UserService2 {
    @Resource
    private UserDao2 userDao;
    @Override
    public void addUser(User3 user)throws SQLException {
         userDao.addUser(user);
    }

    @Override
    public int delUser(int id)   {return userDao.delUser(id);}

    @Override
    public int update(User3 user) {
        return userDao.update(user);
    }

    @Override
    public User3 findOne(int id) {
        return userDao.findOne(id);
    }

    @Override
    public List<User3> findList() {
        return userDao.findList();
    }
}
