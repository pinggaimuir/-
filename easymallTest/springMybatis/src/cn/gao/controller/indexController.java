package cn.gao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by tarena on 2016/10/10.
 */
@Controller
public class indexController {
    @RequestMapping("/")
    public String getIndex(){
        return "index";
    }
}
