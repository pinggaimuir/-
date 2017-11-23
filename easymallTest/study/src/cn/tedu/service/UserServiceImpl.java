package cn.tedu.service;

import cn.tedu.dao.UserDao;
import cn.tedu.domain.User;
import cn.tedu.exception.MsgException;
import cn.tedu.factory.BasicFactory;

/**
 * Created by tarena on 2016/9/3.
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao= BasicFactory.getFactory().getInstance(UserDao.class);
    /**
     * 故居用户提供的信息进行用户注册
     * @param user 用户名
     */
    public void regist(User user) throws MsgException {
        if(userDao.findUserByUname(user.getUsername())!=null) {
            throw new MsgException("用户名已经存在");
        }
            userDao.addUser(user);
    }

    /**
     * 根据用户名和密码登陆
     * @param username 用户名
     * @param password 密码
     */
    public User login(String username,String password){
         return userDao.findUserByUnamePwd(username,password);
    }


    /**
     * 跟据用户名查找用户是否存在
     * @param username 用户名
     * @return 存在返回用户的对象信息，不存在返回null
     */
    public User findByUsername(String username) {
        return userDao.findUserByUname(username);
    }
}
