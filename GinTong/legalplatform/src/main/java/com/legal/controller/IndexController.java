package com.legal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 高健 on 2017/3/12.
 */
@Controller
public class IndexController {
    @RequestMapping("index")	//全局唯一
    public String index(){
        return "forward:/lawinfo/借贷/1/search.html";
    }
}
