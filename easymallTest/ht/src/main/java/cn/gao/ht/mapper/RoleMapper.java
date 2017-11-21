package cn.gao.ht.mapper;

import cn.gao.ht.pojo.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by tarena on 2016/10/17.
 */
public interface RoleMapper {
    @Select(value="select * from role_p")
    List<Role> findRoleList();
    /*删除角色信息*/
    void deleteRole(String[] roleIds);
    /*插入角色信息*/
    void saveRole(Role role);
    /*根据id查找角色*/
    @Select(value="select * from role_p where role_id=#{roleId}")
    Role findRoleByPrimaryKey(String roleId);
    /*根据id修改角色信息*/
    void updateRole(Role role);
     /*根据用户id查找用户所拥有的角色*/
    List<String> findUserRoleByUserId(String userId);
    /*保存角色的权限信息*/
    void saveRoleMudule(@Param("roleId") String roleId,@Param("moduleId") String moduleId);
    /*根据用户id删除用户所拥有的权限*/
    void deleteUserRoleByUserId(String userId);
//    /*根据用户名查询用户所拥有的权限*/
//    List<Role> findUserRoleByUsername(String username);
}
