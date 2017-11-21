package cn.gao.ht.service;

import cn.gao.ht.mapper.RoleMapper;
import cn.gao.ht.mapper.UserInfoMapper;
import cn.gao.ht.mapper.UserMapper;
import cn.gao.ht.pojo.Role;
import cn.gao.ht.pojo.User;
import cn.gao.ht.pojo.UserInfo;
import cn.gao.ht.tool.MdrHash;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tarena on 2016/10/15.
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RoleMapper roleMapper;

    /**
     * 查找中户列表
     */
    @Override
    public List<User> findUserList() throws Exception {
        return userMapper.findUserList();
    }

    /**
     * 修改用户状态
     */
    public void updateUserState(String[] userIds, Integer state) {
        for(String userId:userIds) {
            userMapper.updateUserState(userId, state);
        }
    }

    /**
     * 删除用户
     */
    public void deleteUser(String[] userIds) {
        for(String userId:userIds){
            userMapper.deleteUser(userId);
        }
        userInfoMapper.deleteUserInfo(userIds);
    }

    /**
     * 添加用户
     */
    public void insertUser(User user) throws Exception {
        //加盐
        user.setPassword(MdrHash.getMd5Hash(user.getPassword(),user.getUsername()));
        userMapper.saveUser(user);
        UserInfo userInfo=user.getUserInfo();
        userInfo.setUserInfoId(user.getUserId());
        userInfoMapper.saveUserInfo(userInfo);
    }

    /**
     * 查找用户扩展信息列表
     */
    public List<UserInfo> finUserInfoList() {
        return userMapper.finUserInfoList();
    }
    /*
    * 根据用户id查找用户信息
    * */
    public User findUserById(String userId) {
        return userMapper.findUserByPrimaryKey(userId);
    }

    @Override
    public void updateUser(String userId, User user) {
        userMapper.updateUser(user);
        UserInfo userInfo=user.getUserInfo();
        userInfo.setUserInfoId(userId);
        userInfoMapper.updateUserInfo(userInfo);
    }
    /*返回角色列表*/
    public List<Role> finRoleList() {
        return roleMapper.findRoleList();
    }
    /**
     * 保存用户角色
     */
    public void saveUserRole(String userId, String[] roleIds) {
        //先删除用户原来的所有权限条目
        roleMapper.deleteUserRoleByUserId(userId);
        //然后增加用户的权限
        for(String roleId:roleIds){
            userMapper.saveUserRole(userId,roleId);
        }
    }

    /*根据用户id查找用户所拥有的角色*/
    public List<String> findRoleIdsByUserId(String userId) {
        return roleMapper.findUserRoleByUserId(userId);
    }
    /*通过用户名查找用户*/
    public User findUserByUserName(String username) {
        return userMapper.findUserByUserName(username);
    }
    /**
     * 通过用户名查询用户所拥有的角色信息
     */
    public List<String> findRoleNamesByUsername(String username){
        List<String> roleNames= userMapper.findRoleNamesByUsername(username);
        return roleNames;
    }

    /**
     * 通过用户名查询用户所拥有的所有权限
     */
    @Override
    public List<String> findModuleNameByUsername(String username) {
        List<String> moduleNames=userMapper. findModuleNameByUsername(username);
        return moduleNames;
    }
}
