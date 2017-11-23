package biz.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;

import util.Constant;
import biz.entity.SessionBean;

import common.service.BaseService;

public class BaseAction {

	protected static Logger log = Logger.getLogger(BaseService.class);

	protected static String SUCCESS = "common/success";
	protected static String ERROR = "common/error";

	// protected HttpServletRequest request =
	// ((ServletRequestAttributes)RequestContextHolder
	// .getRequestAttributes()).getRequest();

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;

	@ModelAttribute
	protected void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();

	}

	protected Object getSessionUser() {
		SessionBean sb = (SessionBean) session.getAttribute(Constant.SESSION_BEAN);
		return sb.getUser();
	}
}
