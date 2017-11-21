package cn.gao.ht.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	//转向欢迎页面
	@RequestMapping("/home")
	public String home(){
		return "/home/fmain";
	}
	
	//转向tilte标题栏页面
	@RequestMapping("/title")
	public String title(){
		return "/home/title";
	}
	
	//转向home的左侧页面
	@RequestMapping("/{module}/left")
	public String homeLeft(@PathVariable String module){
		return "/"+module+"/left";
	}
	
	//转向home的操作页面
	@RequestMapping("/{module}/main")
	public String homeMain(@PathVariable String module){
		return "/"+module+"/main";
	}

}
