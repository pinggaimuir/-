package cn.gao.ht.controller;

import cn.gao.ht.pojo.Module;
import cn.gao.ht.pojo.Role;
import cn.gao.ht.service.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tarena on 2016/10/17.
 */
@Controller
@RequestMapping("/sysadmin/role/")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;
    //角色列表查询
    @RequestMapping("list")
    public String tolist(Model model) throws Exception {
        List<Role> rolelist=roleService.findRoleList();
        model.addAttribute("roleList",rolelist);
        return "sysadmin/role/JRoleList";
    }
    /**
     * 角色删除
     */
    @RequestMapping("delete")
    public String delete(@RequestParam("roleId") String[] roleIds) throws Exception {
        roleService.deleteRole(roleIds);
        return "redirect:/sysadmin/role/list";
    }
    /**
     * 角色增加
     */
    @RequestMapping("tocreate")
    public String tocreate() throws Exception {
        return "sysadmin/role/JCreateRole";
    }

    /**
     * 角色新增提交保存
     */
    @RequestMapping("saveRole")
    public String saveRole(Role role) throws Exception {
        roleService.saveRole(role);
        return "redirect:/sysadmin/role/list";
    }
    /**
     * 查看角色信息
     */
    @RequestMapping("toview")
    public String viewRole(String roleId,Model model) throws Exception {
        if(roleId==null||roleId.trim().equals("")){
            model.addAttribute("message","请选择角色id");
            return "forward:/sysadmin/role/list";
        }
        Role role=roleService.findRoleByPrimaryKey(roleId);
        model.addAttribute("role",role);
        return "sysadmin/role/JViewRole";
    }
    /**
     * 准备角色修改信息，并且跳转到角色修改页面
     */
    @RequestMapping("toupdate")
    public String toupdate(String roleId,Model model) throws Exception {
        if(roleId==null||roleId.trim().equals("")){
            model.addAttribute("message","请选择角色id");
            return "forward:/sysadmin/role/list";
        }
        Role role=roleService.findRoleByPrimaryKey(roleId);
        model.addAttribute("role",role);
        return "sysadmin/role/JUpdateRole";
    }

    /**
     * 角色修改信息提交
     */
    @RequestMapping("updateRole")
    public String updateRole(Role role,Model model) throws Exception {
        roleService.updateRole(role);
        return "redirect:/sysadmin/role/list";
    }
    /**
     * 返回json字符串
     * 为角色分配权限
     */
    @RequestMapping("toRoleModile")
    public String toRoleModile(String roleId,Model model) throws JsonProcessingException {
        List<Module> moduleList=roleService.findModuleList();
        List<String> moduleIds=roleService.findModuleIdsByroleId(roleId);
        /*将角色用的权限设置为checked*/
        for(Module module:moduleList){
            if(moduleIds.contains(module.getModuleId())){
                module.setChecked(true);
            }
        }
        //将权限列表信息转换为json
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(moduleList);
        model.addAttribute("json",json);
        model.addAttribute("roleId",roleId);
        model.addAttribute("moduleIds",moduleIds);
        return "sysadmin/role/JRoleModule";
    }
    /*保存角色权限信息*/
    @RequestMapping("saveRoleModule")
    public String saveRoleModule(String roleId,String[] moduleIds){
        roleService.saveRoleMudule(roleId,moduleIds);
        return "redirect:/sysadmin/role/list";
    }
}
