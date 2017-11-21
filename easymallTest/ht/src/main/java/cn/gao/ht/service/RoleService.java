package cn.gao.ht.service;

import cn.gao.ht.pojo.Module;
import cn.gao.ht.pojo.Role;

import java.util.List;

/**
 * Created by tarena on 2016/10/17.
 */

public interface RoleService {
    /*查询角色列表*/
    List<Role> findRoleList()throws Exception;
    /*删除角色*/
    void deleteRole(String[] roleIds);
    /*保存角色*/
    void saveRole(Role role);
    /*查看单个角色*/
    Role findRoleByPrimaryKey(String roleId);
    /*修改角色信息*/
    void updateRole (Role role);
    /*查询全部权限列表*/
    List<Module> findModuleList();
    /*通过角色id查询角色拥有的所有权限*/
    List<String> findModuleIdsByroleId(String roleId);
    /*保存角色的权限信息*/
    void saveRoleMudule(String roleId, String[] moduleIds);
}
