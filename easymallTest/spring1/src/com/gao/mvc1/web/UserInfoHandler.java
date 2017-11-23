package com.gao.mvc1.web;

import com.gao.mvc1.domain.User22;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarena on 2016/9/30.
 */
@Controller
public class UserInfoHandler {
    private List<User22> list=new ArrayList();
    @RequestMapping("/addUser3.do")
    public ModelAndView addUser(User22 user,User22 name){
        ModelAndView modelAndView=new ModelAndView();
        System.out.println("addUser:"+name);
        list.add(user);
        modelAndView.addObject("userList",list);
        modelAndView.setViewName("mvc/showUser");
        return modelAndView;
    }
    @RequestMapping("/accessable.do")
    public String redirectIndex(String name, ModelMap map){
//        System.out.println(name);
//        map.addAttribute("name",name);
        User22 user=new User22();
        user.setId(15);
       map.addAttribute("name",user);

/*        return "forward:WEB-INF/mvc/addUser.jsp";
        return "mvc/addUser";*/
        return "redirect:/addUser3.do";
    }
    @RequestMapping("/show3.do")
    public void show(){

    }
}
