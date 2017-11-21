package cn.gao.ht.mapper;

import cn.gao.ht.pojo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tarena on 2016/10/17.
 */
public interface ModuleMapper {

    List<Module> findModuleList();
    //删除模块
    void deleteModule(String[] modules);
    /*通过主键查找模块信息*/
    Module findModuleByPrimaryKey(String moduleId);
    /*插入模块信息*/
    void saveModule(Module module)throws Exception;
    /*通过主键更新模块信息*/
    void updateModule(Module module)throws Exception;
    /* 通过主键修改模块状态*/
    void updateModuleState(@Param("moduleIds") String[] moduleId,@Param("state") Integer state)throws Exception;
    /*查找所有启用的模块*/
    List<Module> findModuleListState();
    /*通过角色id查询角色拥有的所有权限*/
    List<String> findModuleIdsByroleId(String roleId);
    /*通过角色id删除角色所拥有的所有权限*/
    void deleteRoleModuleByRoleId(String roleId);
}
