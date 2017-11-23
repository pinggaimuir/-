package com.gao.mvc1.web;

import com.gao.mvc1.domain.User11;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarena on 2016/9/28.
 */
@Controller
public class UserHandler {
    private List<User11> userList=new ArrayList();
    @RequestMapping("/addUser2.do")
    public String addUser(User11 user, ModelMap model){

        System.out.println(user);
        userList.add(user);
        model.addAttribute("userList",userList);
        return "mvc/showUser";
    }
    @RequestMapping("/show.do")
    public void show(){

    }
    @InitBinder
    public void InitBinder (ServletRequestDataBinder binder){
        binder.registerCustomEditor(
                java.util.Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
}
