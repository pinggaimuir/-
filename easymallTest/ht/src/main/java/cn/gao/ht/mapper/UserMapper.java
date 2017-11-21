package cn.gao.ht.mapper;

import cn.gao.ht.pojo.User;
import cn.gao.ht.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tarena on 2016/10/15.
 */
public interface UserMapper {
    List<User> findUserList()throws Exception;

    void updateUserState(@Param("userId") String userId,@Param("state")  Integer state);

    void deleteUser(String userId);

    void saveUser(User user);

    List<UserInfo> finUserInfoList();
    /*通过用户主键查找用户信息*/
    User findUserByPrimaryKey(String userId);

    void updateUser(User user);

    void saveUserRole(@Param("userId") String userId,@Param("roleId") String roleId);
    /*通过用户名查找用户*/
    User findUserByUserName(String username);
    /*通过用户名查咋哄用户拥有的角色名称*/
    List<String> findRoleNamesByUsername(String username);
    /**
     * 通过用户名查询用户所拥有的所有权限
     */
    List<String> findModuleNameByUsername(String username);
}
