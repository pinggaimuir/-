package cn.gao.contraller;

import cn.gao.pojo.User;
import cn.gao.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/10/13.
 */

public class LoginController {
    @Resource
    private UserService userService;
    @RequestMapping("login")
    public ModelAndView login(@RequestParam("username") String username,@RequestParam("password") String password){
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        Subject subject= SecurityUtils.getSubject();
        try {
            subject.login(token);
        }catch (IncorrectCredentialsException ice){
            //密码错误异常
            ModelAndView mv=new ModelAndView("error");
            mv.addObject("message","password error");
            return mv;
        }catch (UnknownAccountException uae){
            //未知用户名错误
            ModelAndView mv=new ModelAndView("error");
            mv.addObject("message","username error");
            return mv;
        }catch (ExcessiveAttemptsException eae){
            //登陆过多错误
            ModelAndView mv=new ModelAndView("error");
            mv.addObject("message","timeserror");
            return mv;
        }
        User user=userService.finByUserName(username);
        subject.getSession().setAttribute("user",user);
        return new ModelAndView("success");
    }

    /**
     * /authc/**=authc
     * 只有登陆了的用户才能访问
     * @return
     */
    @RequestMapping("anyuser")
    public ModelAndView anyuser(){
        Subject subject=SecurityUtils.getSubject();
        User user= (User) subject.getSession().getAttribute("user");
        return new ModelAndView("inner");
    }

    /**
     * /authc/admin=user[admin]
     * 只有具备admin角色的用户才可以访问，否则会被重定向至登陆页面
     * @return
     */
    @RequestMapping("admin")
    public ModelAndView admin(){
        User user= (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        System.out.println(user);
        return new ModelAndView("inner");
    }
}
