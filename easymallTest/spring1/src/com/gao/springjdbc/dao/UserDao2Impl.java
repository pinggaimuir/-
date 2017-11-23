package com.gao.springjdbc.dao;

import com.gao.springjdbc.domain.User3;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by tarena on 2016/9/24.
 */
@Repository
public class UserDao2Impl implements UserDao2 {
    @Resource
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User3> rowMapper=new RowMapper<User3>() {
        public User3 mapRow(ResultSet rs, int i) throws SQLException {
            User3 user = new User3();
            user.setId(rs.getInt("id"));
            user.setSalary(rs.getInt("salary"));
            user.setName(rs.getString("name"));
            user.setSex(rs.getString("sex"));
            return user;
        }
    };
    @Override
    public void addUser(User3 user)throws SQLException {
        String sql="insert into user3 values(?,?,?,?)";
        jdbcTemplate.update(sql,user.getId(),user.getName(),user.getSalary(),user.getSex());
        throw new SQLException();
    }

    @Override
    public int delUser(int id) {
        String sql="delete from user3 where id=?";
        return jdbcTemplate.update(sql,id);
    }

    @Override
    public int update(User3 user) {
        String sql="update user3 set name=?,salary=?,sex=? where id=?";
        return jdbcTemplate.update(sql,user.getName(),user.getSalary(),user.getSex(),user.getId());
    }

    @Override
    public User3 findOne(int id) {
        String sql="select * from user3 where id=?";
        List<User3> list= jdbcTemplate.query(sql,rowMapper,id);
        User3 user=null;
        for(User3 u:list){
            user=u;
        }
        return user;
    }

    @Override
    public List<User3> findList() {
        String sql="select * from user3";
        return jdbcTemplate.query(sql,rowMapper);
    }
}
