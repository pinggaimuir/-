package com.gao.mvc1.web;

import com.gao.mvc1.domain.User11;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tarena on 2016/9/21.
 */
@Controller
@RequestMapping("/mvc1")
public class RequestMappingTest1 {
    private Map<Integer,User11> usermap=new HashMap();
    @RequestMapping("/hello1.do")
    public ModelAndView execute(Model m)throws Exception{
        ModelAndView mv=new ModelAndView();
        mv.addObject("hello","wo ai zu guo");
        mv.addObject("hello2","zuguo sheng ri kuai le ");
        Map<String,String> modelmap=new HashMap();
        modelmap.put("gao","jian");
        mv.addObject(modelmap);
        mv.setViewName("hello");

        System.out.println(mv.getView());
        return mv;
    }
    @RequestMapping("/addUser.do")
    public String execute2(User11 user, ModelMap model)throws Exception{
        System.out.println(user.toString());
        model.addAttribute("user",user);
        return "forward:hello1.do";
    }
//    @RequestMapping("/addUser.do")
    public String execute3(@RequestParam("user")String user1, ModelMap model)throws Exception{
        System.out.println(user1.toString());
        model.addAttribute("user",user1);
        return "mvc/addUser";
    }
//    @RequestMapping("/addUser.do")
    public String execute4(@DateTimeFormat(pattern = "yyyy-MM-dd")Date date, ModelMap model)throws Exception{
        model.addAttribute("user",date);
        return "mvc/addUser";
    }
    /*日期转换器*/
    @InitBinder
    public void InitBinder (ServletRequestDataBinder binder){
        binder.registerCustomEditor(
                java.util.Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

}
