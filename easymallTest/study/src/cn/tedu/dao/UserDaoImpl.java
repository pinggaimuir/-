package cn.tedu.dao;

import cn.tedu.domain.User;
import cn.tedu.utils.DaoUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * Created by tarena on 2016/9/5.
 */
public class UserDaoImpl implements UserDao{
    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return user对象
     */
    public User findUserByUname(String username) {
        String sql="SELECT  *  FROM  user WHERE username=?";
        Object[] params={username};
        try {
            QueryRunner qr=new QueryRunner(DaoUtils.getSource());
            return qr.query(sql,new BeanHandler<User>(User.class),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 向数据库插入用户信息
     * @param user user对象
     * @return 是否插入成功，成功返回true，否则返回false
     */
    public boolean addUser(User user) {
        String sql="INSERT INTO user (username,password,nickname,email)  VALUES (?,?,?,?)";
        Object[] params={user.getUsername(),user.getPassword(),user.getNickname(),user.getEmail()};
        try {
            QueryRunner qr=new QueryRunner(DaoUtils.getSource());
            if(qr.update(sql,params)>0){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 同伙用户名和密码查找用户
     * @param username 用户名
     * @param password 密码
     * @return 用户信息或者null
     */
    public User findUserByUnamePwd(String username, String password) {
        String sql="select * from user where username=? and password=?";
        Object[] params={username,password};
        try {
            QueryRunner qr=new QueryRunner(DaoUtils.getSource());
            return qr.query(sql,new BeanHandler<User>(User.class),params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
