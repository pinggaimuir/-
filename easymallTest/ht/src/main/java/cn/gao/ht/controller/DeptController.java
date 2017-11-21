package cn.gao.ht.controller;

import cn.gao.ht.pojo.Dept;
import cn.gao.ht.service.DeptService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tarena on 2016/10/14.
 */
@Controller
@RequestMapping("sysadmin/dept/")
public class DeptController {
    @Resource
    private DeptService deptServive;
    @RequestMapping("list")
    public String toDeptList(Model model) throws Exception {
        model.addAttribute("deptList",deptServive.findDeptList());
        return "/sysadmin/dept/jDeptList";
    }
    //部门状态停用
    @RequestMapping("stop")
    public String toStop(@RequestParam("deptId") Integer[] deptIds){
        deptServive.updateState(deptIds,0);
        return "redirect:/sysadmin/dept/list";
    }
    /*部门状态启用*/
    @RequestMapping("start")
    public String toStart(@RequestParam("deptId") Integer[] deptIds){
        deptServive.updateState(deptIds,1);
        return "redirect:/sysadmin/dept/list";
    }
    /*删除部门*/
    @RequestMapping("delete")
    public String todelete(@RequestParam("deptId") Integer[] deptIds){
        deptServive.deleteDept(deptIds);
        return "redirect:/sysadmin/dept/list";
    }
    /*跳转到创建部门*/
    @RequestMapping("tocreate")
    public String tocreate(Model model) throws Exception {
        List<Dept> deptList=deptServive.findDeptList();
        model.addAttribute("deptList",deptList);
        return "/sysadmin/dept/jCreateDept";
    }
    /*新建部门信息提交*/
    @RequestMapping("saveDept")
    public String saveDept(Dept dept){
        deptServive.saveDept(dept);
        return "redirect:/sysadmin/dept/list";
    }
    /* 查看部门信息*/
    @RequestMapping("toview")
    public String viewDept(String deptId,Model model){
        Dept dept=deptServive.selectDeptOne(deptId);
        model.addAttribute("dept",dept);
        return "/sysadmin/dept/jViewDept";
    }
    /*修改部门信息*/
    @RequestMapping("toupdate")
    public String toupdate(String deptId,Model model){
        //准备要修改的数据
        Dept dept=deptServive.selectDeptOne(deptId);
        //准备部门下拉列表
        List<Dept> deptList=deptServive.getDeptList();
        model.addAttribute("dept",dept);
        model.addAttribute("deptList",deptList);
        return "/sysadmin/dept/jUpdateDept";
    }
    /*修改部门信息的提交保存*/
    @RequestMapping("updateDept")
    public String updateDept(Dept dept,Model model){
        deptServive.updateDept(dept);
        model.addAttribute("deptId",dept.getDeptId());
        return "forward:/sysadmin/dept/toview";
    }
}
