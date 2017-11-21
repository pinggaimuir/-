package cn.gao.ht.controller;

import cn.gao.ht.pojo.User;
import cn.gao.ht.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by tarena on 2016/10/18.
 */
@Controller
public class LoginController extends BaseController {
    @Resource
    private UserService userService;
    @RequestMapping("/validate/login")
    public String doLogin(String username,String password,HttpSession session){
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        token.setRememberMe(true);
        Subject subject= SecurityUtils.getSubject();
        try{
            subject.login(token);
        }catch (IncorrectCredentialsException ice){
            //密码错误异常
            session.setAttribute("loginFailed",1);
            return "redirect:/login.jsp";
        }catch (UnknownAccountException uae){
            //未知账户异常
            session.setAttribute("loginFailed",2);
            return "redirect:/login.jsp";
        }catch (ExcessiveAttemptsException eae){
            //登陆超过5次异常
            session.setAttribute("errorInfo","登陆次数过多！");
            return "redirect:/login.jsp";
        }
        User user=userService.findUserByUserName(username);
        subject.getSession().setAttribute("user",user);
        return "redirect:/home";
//        /**
//         * 用户名密码为空
//         */
//        if(StringUtils.isNullOrEmpty(username)||StringUtils.isNullOrEmpty(password)){
//            session.setAttribute("loginFailed",2);
//            return "login";
//        }
//        /**
//         * 如果用户名或者密码正确
//         */
//        User user=userService.findUserByUserName(username);
//        if(user!=null&&password.equals(user.getPassword())){
//            return "redirect:/index.jsp";
//        }
//        //不正确则返回登陆页面
//        session.setAttribute("loginFailed",1);
//        return "login";
    }
}
