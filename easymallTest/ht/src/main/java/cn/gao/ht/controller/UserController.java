package cn.gao.ht.controller;

import cn.gao.ht.pojo.Dept;
import cn.gao.ht.pojo.Role;
import cn.gao.ht.pojo.User;
import cn.gao.ht.pojo.UserInfo;
import cn.gao.ht.service.DeptService;
import cn.gao.ht.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tarena on 2016/10/15.
 */
@Controller
@RequestMapping("/sysadmin/user/")
public class UserController extends BaseController{
    @Resource
    private UserService userService;
    @Resource
    private DeptService deptService;

    /**
     * 返回用户列表
     */
    @RequestMapping("list")
    public String toUserList(Model model) throws Exception {
        List<User> userList=userService.findUserList();
        model.addAttribute("userList",userList);
        return "sysadmin/user/JUserList";
    }
    /**
     * 停滞用户
     */
    @RequestMapping("stop")
    public String stop(@RequestParam("userId") String[] userIds){
        userService.updateUserState(userIds,0);
        return "redirect:/sysadmin/user/list";
    }

    /**
     * 启用用户
     */
    @RequestMapping("start")
    public String start(@RequestParam("userId") String[] userIds){
        userService.updateUserState(userIds,1);
        return "redirect:/sysadmin/user/list";
    }
    /**
     * 删除用户
     */
    @RequestMapping("delete")
    public String delete(@RequestParam("userId") String[] userIds){
        userService.deleteUser(userIds);
        return "redirect:/sysadmin/user/list";
    }

    /**
     * 准备部门列表和领导人列表，跳转到新增用户页面
     */
    @RequestMapping("tocreate")
    public String tocreate(Model model) throws Exception {
        List<Dept> deptList=deptService.findDeptList();
        List<UserInfo> userInfoList=userService.finUserInfoList();
        model.addAttribute("deptList",deptList);
        model.addAttribute("userInfoList",userInfoList);
        return "sysadmin/user/JCreateUser";
    }

    /**
     * 新增用户保存
     */
    @RequestMapping("saveUser")
    public String saveUser(@Validated User user, BindingResult result,
                           Model model) throws Exception {
        if(result.hasErrors()){
            List<ObjectError> errors=result.getAllErrors();
            model.addAttribute("errors",errors);
            return "forward:/sysadmin/user/tocreate";
        }
        userService.insertUser(user);
        return "redirect:/sysadmin/user/list";
    }

    /**
     * 查看用户信息
     */
    @RequestMapping("toview")
    public String toview(String userId,Model model) throws Exception {
        model.addAttribute("user",userService.findUserById(userId));
        return "sysadmin/user/JViewUser";
    }

    /**
     * 准备要修改的用户信息
     */
    @RequestMapping("toupdate")
    public String toupdate(String userId,Model model) throws Exception {
        List<Dept> deptList=deptService.findDeptList();
        List<UserInfo> userInfoList=userService.finUserInfoList();
        model.addAttribute("deptList",deptList);
        model.addAttribute("userInfoList",userInfoList);
        model.addAttribute("user",userService.findUserById(userId));
//        userService.insertUser(user);
        return "sysadmin/user/JUpdateUser";
    }

    /**
     * 修改用户的信息的提交
     */
    @RequestMapping("updateUser")
    public String updateUser(User user) throws Exception {
        userService.updateUser(user.getUserId(),user);
        return "redirect:/sysadmin/user/list";
    }

    /**
     *用户角色管理
     */
    @RequestMapping("toUserRole")
    public String toUserRole(String userId,Model model) throws Exception {
        List<Role> roleList=userService.finRoleList();
        List<String> roleIds=userService.findRoleIdsByUserId(userId);
        //将用户所拥有的角色checked属性设为true
        for(Role role:roleList){
            if(roleIds.contains(role.getRoleId())){
                role.setChecked(true);
            }
        }
        //把数组转换成json
        ObjectMapper objectMapper=new ObjectMapper();
        String json=objectMapper.writeValueAsString(roleList);
        model.addAttribute("json",json);
        model.addAttribute("userId",userId);
        return "sysadmin/user/JUserRole";
    }

    /**
     * 保存用户角色
     */
    @RequestMapping("saveUserRole")
    public String saveUserRole(String userId,String[] roleIds) throws Exception {
        userService.saveUserRole(userId,roleIds);
        return "redirect:/sysadmin/user/list";
    }
}
