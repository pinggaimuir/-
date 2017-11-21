package cn.gao.ht.controller;

import cn.gao.ht.pojo.BaseEntity;
import cn.gao.ht.pojo.Module;
import cn.gao.ht.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 模块信息管理
 * Created by tarena on 2016/10/17.
 */
@Controller
@RequestMapping("sysadmin/module/")
public class ModuleController extends BaseEntity {
    @Resource
    private ModuleService moduleService;

    /**
     * 模块列表显示
     */
    @RequestMapping("list")
    public String moduleList(Model model){
        List<Module> moduleList=moduleService.findModuleList();
        model.addAttribute("list",moduleList);
        return "sysadmin/module/JModuleList";
    }
    /**
     * 删除模块
     */
    @RequestMapping("delete")
    public String deleteModule(@RequestParam("moduleId") String[] modules){
        moduleService.deleteModele(modules);
        return "redirect:/sysadmin/module/list";
    }

    /**
     * 查看模块
     */
    @RequestMapping("toview")
    public String  toview(String moduleId,Model model) {
        if(moduleId==null||moduleId.trim().equals("")){
            model.addAttribute("message","请选择一个模块");
            return "forward:/sysadmin/module/list";
        }
        Module module=moduleService.findModuleByPrimaryKey(moduleId);
        model.addAttribute("module",module);
        return "sysadmin/module/JViewModule";
    }
    /**
     * 新增模块
     */
    @RequestMapping("tocreate")
    public String tocreate(Model model){
        List<Module> moduleList=moduleService.findModuleList();
        model.addAttribute("list",moduleList);
        return "sysadmin/module/JCreateModule";
    }

    /**
     * 新增模块保存
     */
    @RequestMapping("saveModule")
    public String saveModule(Module module) throws Exception {
        moduleService.saveModule(module);
        return "redirect:/sysadmin/module/list";
    }

    /**
     * 准备修改信息，跳转到修改页面
     */
    @RequestMapping("toupdate")
    public String toupdate(String moduleId,Model model) throws Exception {
        if(moduleId==null||moduleId.trim().equals("")){
            model.addAttribute("message","请选择一个模块");
            return "forward:/sysadmin/module/list";
        }
        List<Module> moduleList=moduleService.findModuleList();
        Module module=moduleService.findModuleByPrimaryKey(moduleId);
        model.addAttribute("module",module);
        model.addAttribute("list",moduleList);
        return "sysadmin/module/JUpdateModule";
    }

    /**
     * 修改信息提交保存
     */
    @RequestMapping("updateModule")
    public String updateModule(Module module) throws Exception {
        moduleService.updateModule(module);
        return "redirect:/sysadmin/module/list";
    }

    /**
     * 开启模块状态
     */
    @RequestMapping("start")
    public String start(@RequestParam("moduleId") String[] moduleIds) throws Exception {
        moduleService.updateModuleState(moduleIds,1);
        return "redirect:/sysadmin/module/list";
    }

    /**
     * 关闭模块状态
     */
    @RequestMapping("stop")
    public String stop(@RequestParam("moduleId") String[] moduleIds) throws Exception {
        moduleService.updateModuleState(moduleIds,0);
        return "redirect:/sysadmin/module/list";
    }

}
