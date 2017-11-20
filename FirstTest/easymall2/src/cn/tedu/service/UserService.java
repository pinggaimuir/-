package cn.tedu.service;

import cn.tedu.domain.User;
import cn.tedu.exception.MsgException;

/**
 * Created by tarena on 2016/9/3.
 */
public interface UserService {
    /**
     * 注册用户
     * @param user 用户名
     * @throws MsgException 用户名错误返回信息
     */
    void regist(User user)throws MsgException;

    /**
     * 用户登陆
     * @param usrname  用户名
     *@param password 密码
     * @return 用户登陆成功返回返回对象信息，不存在返回null
     */
    User login(String usrname,String password);

    /**
     * 跟据用户名查找用户是否存在
     * @param username 用户名
     * @return 存在返回用户的对象信息，不存在返回null
     */
    User findByUsername(String username);

}
