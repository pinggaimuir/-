package cn.gao.ht.service;

import cn.gao.ht.pojo.Module;

import java.util.List;

/**
 * Created by tarena on 2016/10/17.
 */

public interface ModuleService {

    List<Module> findModuleList();
    /*删除模块列表*/
    void deleteModele(String[] modules);
    /*查看模块信息*/
    Module findModuleByPrimaryKey(String moduleId);
    /*插入模块信息*/
    void saveModule(Module module)throws Exception;
    /*通过主键更新模块信息*/
    void updateModule(Module module)throws Exception;
    /*通过主键修改模块状态*/
    void updateModuleState(String[] moduleIds,Integer state)throws Exception;
}
