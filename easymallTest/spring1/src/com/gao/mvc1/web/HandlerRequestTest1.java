package com.gao.mvc1.web;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tarena on 2016/9/21.
 */

public  class HandlerRequestTest1 implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mv=new ModelAndView();
        mv.addObject("hello","wo ai zu guo");
        mv.addObject("hello2","zuguo sheng ri kuai le ");
        Map<String,String> modelmap=new HashMap();
        modelmap.put("gao","jian");
        mv.addAllObjects(modelmap);
        mv.setViewName("hello");
        return mv;
    }
}
