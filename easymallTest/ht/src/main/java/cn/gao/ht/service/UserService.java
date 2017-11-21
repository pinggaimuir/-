package cn.gao.ht.service;

import cn.gao.ht.pojo.Role;
import cn.gao.ht.pojo.User;
import cn.gao.ht.pojo.UserInfo;

import java.util.List;

/**
 * Created by tarena on 2016/10/15.
 */
public interface UserService {

    List<User> findUserList() throws Exception;

    void updateUserState(String[] userIds,Integer state);

    void deleteUser(String[] userIds);

    void insertUser(User user) throws Exception;

    List<UserInfo> finUserInfoList();
    /*根据用户id查找用户信息*/
    User findUserById(String userId);

    void updateUser(String userId,User user);

    List<Role> finRoleList();

    void saveUserRole(String userId, String[] roleIds);
    /*根据用户id查找用户所拥有的角色*/
    List<String> findRoleIdsByUserId(String userId);
    /*通过用户名查找用户所拥有的角色名称*/
    List<String> findRoleNamesByUsername(String username);
    /*通过用户名查询用户所拥有的所有权限*/
    List<String> findModuleNameByUsername(String username);
    /*通过用户名查找用户*/
    User findUserByUserName(String username);
}
