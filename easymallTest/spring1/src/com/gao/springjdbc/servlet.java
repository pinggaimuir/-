package com.gao.springjdbc;

import com.gao.springjdbc.domain.User3;
import com.gao.springjdbc.service.UserService2;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by tarena on 2016/9/24.
 */
@Controller
public class servlet {
    @Resource
    private UserService2 userService;
    public void addUser(User3 user)throws SQLException{
         userService.addUser(user);
    }
    public int delUser(int id){
        return userService.delUser(id);
    }
    public int update(User3 user){
         return userService.update(user);
    }
    public User3 selectOne(int id){
        return userService.findOne(id);
    }
    public List<User3> selectList(){
        return userService.findList();
    }
}
