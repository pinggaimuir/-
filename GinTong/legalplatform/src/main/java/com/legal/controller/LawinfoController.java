package com.legal.controller;

import com.legal.commons.PageBean;
import com.legal.service.LawInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * Created by 高健 on 2017/3/12.
 */
@Controller
@RequestMapping("/lawinfo")
public class LawinfoController {

    @Resource
    private LawInfoService lawInfoService;

    /**
     * 搜索
     * @param keyWord 关键字
     * @param page 当前页
     */
    @RequestMapping("/{keyWord}/{page}/search")
    public String search(@PathVariable("keyWord") String keyWord,
                           @PathVariable("page")  Integer page,
                           Model model){
        try {
            keyWord=new String(keyWord.getBytes("ISO-8859-1"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //默认每页显示10条记录
        int rows=5;
        PageBean bean=lawInfoService.search(keyWord,page,rows);
        model.addAttribute("bean",bean);
        model.addAttribute("keyWord",keyWord);
        return "list";
    }
}
