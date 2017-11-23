package biz.web.action;

import biz.entity.SessionBean;
import biz.entity.main.SimpleUser;
import biz.entity.main.SysUser;
import biz.entity.main.User;
import biz.web.service.impl.BizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import util.Constant;
import util.MD5;
import util.MessageUtil;
import util.StringUtil;

@Controller
@RequestMapping("/com")
public class SignAction extends BaseAction {
	@Autowired
	private BizService service;

    //注册
	@RequestMapping(value = "/reg.do", method = RequestMethod.POST)
	public String reg(SimpleUser bean) throws Exception {
		String msg = "";
		try {
			service.addSimpleUser(bean);
			msg = "注册成功";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "注册失败";
		}
		session.setAttribute("regErrorMessage", msg);
		return "redirect:/reg.jsp";
	}

	/**
	 * 登录
	 * @param loginid 用户名
	 * @param password 密码
	 * @param logintype
	 * @param checkcode 验证码
     */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String login(String loginid, String password, String logintype, String checkcode) throws Exception {
		log.info(loginid + " " + password + " " + logintype);
		String errorMessage = null;
		try {
			String code = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
			if (code == null) {
				request.getSession(false).invalidate();
				return "redirect:/index.jsp";
			} else {
			}
			checkcode = StringUtil.stringVerification(checkcode);
			if (code.toLowerCase().equals(checkcode.toLowerCase())) {
				loginid = StringUtil.stringVerification(loginid).toLowerCase();
				MD5 md = new MD5();
				password = md.getMD5ofStr(password);
				Object user = service.findUser(logintype, loginid, password);
				if (user != null) {

					SessionBean sb = new SessionBean();
					sb.setUser(user);

					session.setAttribute(Constant.SESSION_BEAN, sb);

					log.info("登录成功:" + loginid);

				} else {
					errorMessage = "登录帐号或者密码错误";
				}
			} else {
				errorMessage = "验证码错误";
			}
			if (errorMessage == null) {
				return "main";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (errorMessage == null) {
			errorMessage = "登录失败,请稍候重试";
		}
		session.setAttribute("signErrorMessage", errorMessage);
		return "redirect:/index.jsp";
	}

	@RequestMapping(value = "/toSelf.do")
	public String toSelf() {
		return "modifySelf";
	}

	@RequestMapping(value = "/modifySelf.do", method = RequestMethod.POST)
	public String modifySelf(User bean) {
		try {
			User sessionUser = null;
			SessionBean sb = (SessionBean) session.getAttribute(Constant.SESSION_BEAN);
			Object sbuser = sb.getUser();
			if (SysUser.class.getSimpleName().equals(sb.getRole())) {
				sessionUser = ((SysUser) sbuser).getUser();
			} else if (SimpleUser.class.getSimpleName().equals(sb.getRole())) {
				sessionUser = ((SimpleUser) sbuser).getUser();
			}
			bean.setUserId(sessionUser.getUserId());
			bean.setUname(sessionUser.getUname());
            //用户密码加密
			if (StringUtil.notEmpty(bean.getUserPassword())) {
				MD5 md = new MD5();
				String password = md.getMD5ofStr(bean.getUserPassword());
				bean.setUserPassword(password);
			} else {
				bean.setUserPassword(sessionUser.getUserPassword());
			}
            //修改数据库中用户信息
			service.update(bean);
            //将修改后的用户信息放入sesion中
			sessionUser.setUserAddress(bean.getUserAddress());
			sessionUser.setUserBirth(bean.getUserBirth());
			sessionUser.setUserEmail(bean.getUserEmail());
			sessionUser.setUserGender(bean.getUserGender());
			sessionUser.setUserName(bean.getUserName());
			sessionUser.setUserPhone(bean.getUserPhone());

			MessageUtil.addMessage(request, "修改成功.");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addMessage(request, "修改失败.");
			return ERROR;
		}
	}

	/**
	 * 注销
	 * @return
     */
	@RequestMapping(value = "/logout.do")
	public String logout() {
		session.invalidate();
		return "redirect:/index.jsp";
	}


}

