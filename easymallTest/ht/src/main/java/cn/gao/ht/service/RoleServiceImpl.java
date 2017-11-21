package cn.gao.ht.service;

import cn.gao.ht.mapper.ModuleMapper;
import cn.gao.ht.mapper.RoleMapper;
import cn.gao.ht.pojo.Module;
import cn.gao.ht.pojo.Role;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tarena on 2016/10/17.
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private ModuleMapper moduleMapper;
    @Override
    public List<Role> findRoleList() throws Exception {
        return roleMapper.findRoleList();
    }

    @Override
    public void deleteRole(String[] roleIds) {
        roleMapper.deleteRole(roleIds);
    }
    /*保存角色*/
    @Override
    public void saveRole(Role role) {
        roleMapper.saveRole(role);
    }
    /* 查看单个角色信息*/
    @Override
    public Role findRoleByPrimaryKey(String roleId) {
        return roleMapper.findRoleByPrimaryKey(roleId);
    }

    /**
     * 修改角色信息
     */
    @Override
    public void updateRole(Role role) {
        roleMapper.updateRole(role);
    }
    /*查找所有权限列表*/
    @Override
    public List<Module> findModuleList() {
        List<Module> moduleList= moduleMapper.findModuleListState();
        for(Module module:moduleList){
            if(module.getParent()!=null){
                module.setpId(module.getParent().getModuleId());
            }
        }
        return moduleList;
    }

    /*通过角色id查询角色拥有的所有权限*/
    public List<String> findModuleIdsByroleId(String roleId) {

        return moduleMapper.findModuleIdsByroleId(roleId);
    }
    /*保存角色的权限信息*/
    public void saveRoleMudule(String roleId, String[] moduleIds) {
        moduleMapper.deleteRoleModuleByRoleId(roleId);
        for(String moduleId:moduleIds){
            roleMapper.saveRoleMudule(roleId,moduleId);
        }
    }
}
