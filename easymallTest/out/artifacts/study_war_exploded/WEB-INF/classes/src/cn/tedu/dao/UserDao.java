package cn.tedu.dao;

import cn.tedu.domain.User;

/**
 * Created by tarena on 2016/8/29.
 */
public interface UserDao {
    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return user对象
     */
     User findUserByUname(String username);
     /**
     * 向数据库插入用户信息
     * @param user user对象
     * @return 是否插入成功，成功返回true，否则返回false
     */
     boolean addUser(User user);

    /**
     * 同伙用户名和密码查找用户
     * @param username 用户名
     * @param password 密码
     * @return 用户信息或者null
     */
     User findUserByUnamePwd(String username, String password);
}
