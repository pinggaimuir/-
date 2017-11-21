package cn.gao.ht.service;

import cn.gao.ht.mapper.ModuleMapper;
import cn.gao.ht.pojo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tarena on 2016/10/17.
 */
@Service
public class ModuleServiceImpl implements ModuleService{
    @Resource
    private ModuleMapper moduleMapper;
    @Override
    public List<Module> findModuleList() {
        return moduleMapper.findModuleList();
    }
    /*删除模块*/
    public void deleteModele(String[] modules) {
        moduleMapper.deleteModule(modules);
    }
    /*通过主键查找模块信息*/
    public Module findModuleByPrimaryKey(String moduleId) {
        return moduleMapper.findModuleByPrimaryKey(moduleId);
    }
    /*新增模块信息*/
    public void saveModule(Module module) throws Exception {
        moduleMapper.saveModule(module);
    }
    /*通过主键更新模块信息*/
    public void updateModule(Module module) throws Exception {
        moduleMapper.updateModule(module);
    }
    /*通过主键修改模块状态*/
    public void updateModuleState(String[] moduleIds, Integer state) throws Exception {
        moduleMapper.updateModuleState(moduleIds,state);
    }


}
