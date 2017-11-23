package biz.web.action.sys;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import util.Constant;
import util.FieldUtil;
import util.MessageUtil;
import util.Page;
import util.SearchParamBean;
import util.StringUtil;
import biz.entity.main.SimpleUser;
import biz.web.action.BaseAction;
import biz.web.service.impl.BizService;

@Controller
@RequestMapping("/sys")
public class SimpleUserAction extends BaseAction {
	@Autowired
	private BizService service;

	@RequestMapping(value = "/add2SimpleUser.do", method = RequestMethod.GET)
	public String add2() {
		return "sys/addSimpleUser";
	}
	@RequestMapping(value = "/getSimpleUser.do", method = RequestMethod.GET)
	public String get(int uid) {
		try {
			SimpleUser bean = (SimpleUser) service.get(SimpleUser.class, uid);
			request.setAttribute("modifybean", bean);

			return "sys/modifySimpleUser";
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addMessage(request, "获取信息失败.");
			return ERROR;
		}
	}

	@RequestMapping(value = "/deleteSimpleUser.do")
	public String delete(String ids) {
		try {
			service.delete(SimpleUser.class, ids);
			MessageUtil.addRelMessage(request, "删除用户信息成功.", "mainquery");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addMessage(request, "删除用户信息失败.");
			return ERROR;
		}
	}

	@RequestMapping(value = "/addSimpleUser.do")
	public String add(SimpleUser bean) {
		try {
			service.addSimpleUser(bean);
			MessageUtil.addMessage(request, "添加用户成功.");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addMessage(request, "添加用户失败.");
			return ERROR;
		}

	}

	@RequestMapping(value = "/updateSimpleUser.do")
	public String update(SimpleUser bean) {
		try {
			service.updateSimpleUser(bean);
			MessageUtil.addMessage(request, "更新用户成功.");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addMessage(request, "更新用户失败.");
			return ERROR;
		}
	}

	@RequestMapping(value = "/querySimpleUser.do")
	public String query() {
		try {
			int pageNum = 0;
			String t = request.getParameter("pageNum");
			if (StringUtil.notEmpty(t)) {
				pageNum = Integer.valueOf(t);
			}
			Page p = (Page) session.getAttribute(Constant.SESSION_PAGE);
			if (pageNum == 0 || p == null) {
				p = new Page();
				p.setCurrentPageNumber(1);
				p.setTotalNumber(0l);
				p.setItemsPerPage(Constant.SESSION_PAGE_NUMBER);

				// 字段名称集合
				LinkedList<String> parmnames = new LinkedList<String>();
				// 字段值集合
				LinkedList<Object> parmvalues = new LinkedList<Object>();
				// 页面参数集合
				Set parm = request.getParameterMap().entrySet();
				for (Object o : parm) {
					Entry<String, Object> e = (Entry<String, Object>) o;
					String name = e.getKey();// 页面字段名称
					if (name.startsWith("s_")) {
						String fieldValue = request.getParameter(name);// 页面字段值
						if (StringUtil.notEmpty(fieldValue)) {
							name = name.substring(2, name.length());// 实体字段名称
							parmnames.add(name);
							parmvalues.add(FieldUtil.format(SimpleUser.class, name, fieldValue));
						}
					}
				}

				SearchParamBean sbean = new SearchParamBean();
				sbean.setParmnames(parmnames);
				sbean.setParmvalues(parmvalues);

				p.setConditonObject(sbean);
			} else {
				p.setCurrentPageNumber(pageNum);
			}
			Page page = null;
			page = service.find(p, SimpleUser.class);

			session.setAttribute(Constant.SESSION_PAGE, page);
			return "sys/listSimpleUser";
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	

}
