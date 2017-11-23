package biz.web.action;

import biz.entity.main.GoodsObject;
import biz.entity.main.GoodsPrice;
import biz.entity.main.SimpleUser;
import biz.entity.main.User;
import biz.web.service.impl.BizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import util.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Controller
@RequestMapping("/com")
public class WebAction extends BaseAction {
	@Autowired
	private BizService service;

	private SimpleUser getUser() {
		SimpleUser su = (SimpleUser) session.getAttribute("SimpleUser");
		return su;
	}

	@RequestMapping(value = "/user.do", method = RequestMethod.GET)
	public String touser(String type) {
		SimpleUser su = (SimpleUser) session.getAttribute("SimpleUser");
		if (su == null) {
			return "redirect:/com/index.do";
		}
		if (StringUtil.isEmpty(type)) {
			type = "info";
		}
		if ("note".equals(type)) {

		} else if ("score".equals(type)) {
		} else if ("fav".equals(type)) {

		}
		request.setAttribute("type", type);
		return "forward:/web/user.jsp";
	}

	@RequestMapping(value = "/userLogin.do", method = RequestMethod.POST)
	@ResponseBody
	public String userLogin(String name, String password) {

		String msg = "用户名或者密码错误";
		try {
			MD5 md = new MD5();
			password = md.getMD5ofStr(password);
			SimpleUser user = (SimpleUser) service.findUser(SimpleUser.class.getSimpleName(), name, password);
			if (user != null) {
				session.setAttribute("SimpleUser", user);
				msg = "成功";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "{\"msg\":\"" + msg + "\"}";
	}

	@RequestMapping(value = "/userReg.do", method = RequestMethod.POST)
	@ResponseBody
	public String userReg(SimpleUser bean) {
		String msg = "";
		try {
			User user = service.findUser(bean.getUser().getUname());
			if (user != null) {
				msg = "注册失败,账号已经被使用";
			} else {
				service.addSimpleUser(bean);
				msg = "成功";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "注册失败";
		}
		return "{\"msg\":\"" + msg + "\"}";
	}

	@RequestMapping(value = "/userPwd.do", method = RequestMethod.POST)
	@ResponseBody
	public String userPwd(String oldpwd, String newpwd) {
		String msg = "";
		try {
			SimpleUser su = (SimpleUser) session.getAttribute("SimpleUser");
			User sessionUser = su.getUser();
			MD5 md = new MD5();
			String password = md.getMD5ofStr(oldpwd);

			SimpleUser temp = (SimpleUser) service.get(SimpleUser.class, su.getId());
			if (!password.equals(temp.getUser().getUserPassword())) {
				msg = "旧密码错误";
			} else {
				sessionUser.setUserPassword(newpwd);
				service.updateSimpleUser(su);
				msg = "成功";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "修改密码失败";
		}
		return "{\"msg\":\"" + msg + "\"}";
	}

	@RequestMapping(value = "/userUpdate.do", method = RequestMethod.POST)
	public String userUpdate(User bean) {
		try {
			SimpleUser su = (SimpleUser) session.getAttribute("SimpleUser");
			User sessionUser = su.getUser();

			bean.setUserId(sessionUser.getUserId());
			bean.setUname(sessionUser.getUname());
			if (StringUtil.notEmpty(bean.getUserPassword())) {
				MD5 md = new MD5();
				String password = md.getMD5ofStr(bean.getUserPassword());
				bean.setUserPassword(password);
			} else {
				bean.setUserPassword(sessionUser.getUserPassword());
			}
			service.update(bean);

			sessionUser.setUserAddress(bean.getUserAddress());
			sessionUser.setUserBirth(bean.getUserBirth());
			sessionUser.setUserEmail(bean.getUserEmail());
			sessionUser.setUserGender(bean.getUserGender());
			sessionUser.setUserName(bean.getUserName());
			sessionUser.setUserPhone(bean.getUserPhone());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/com/user.do?type=info";
	}

	@RequestMapping(value = "/userLogout.do", method = RequestMethod.GET)
	public String userLogout() {
		session.removeAttribute("SimpleUser");
		return "forward:/web/index.jsp";
	}

	@RequestMapping(value = "/getGoods.do", method = RequestMethod.GET)
	public String getGoods(int uid) {
		GoodsObject item = (GoodsObject) service.get(GoodsObject.class, uid);
		request.setAttribute("item", item);

		List<GoodsPrice> rlist = service.findGoodsPriceList(uid);

		String times[] = new String[rlist.size()];
		//		{
		//            name: '价格',
		//            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
		//        }
		Collections.reverse(rlist);

		float[] prices = new float[rlist.size()];
		for (int i = 0; i < rlist.size(); i++) {
			GoodsPrice p = rlist.get(i);
			times[i] = "'" + p.getAddDate() + "'";
			prices[i] = p.getPrice();
		}

		request.setAttribute("times", Arrays.toString(times));
		request.setAttribute("values", "{name:'价格',data:" + Arrays.toString(prices) + "}");

		return "forward:/web/detailGoods.jsp";
	}

	@RequestMapping(value = "/toSort.do")
	public String toSort(String ids) {
		//根据id查询出id对应的商品列表
		List<GoodsObject> list = service.queryByHQL("from GoodsObject where id in (" + ids + ")");

		Date cdate = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		//创建过去7天日期的字符串放入数组中
		String times[] = new String[7];
		for (int i = 0; i < 7; i++) {
			Date d = DateUtil.addDays(cdate, 0 - i);
			times[i] = "'" + sf.format(d) + "'";
		}
		String values = "";
		//遍历上面的商品列表
		for (GoodsObject goods : list) {
			float[] prices = new float[7];
			//遍历上面的日期数组
			for (int i = 0; i < 7; i++) {
				Date d = DateUtil.addDays(cdate, 0 - i);
				GoodsPrice pricebean = service.getPrice(goods, sf.format(d));
				if (pricebean == null) {
					prices[i] = 0;
				} else {
					prices[i] = pricebean.getPrice();
				}
			}
			values += "{name:'" + goods.getGoodsName() + "',data:" + Arrays.toString(prices) + "},";

		}
		values = values.substring(0, values.length() - 1);

		request.setAttribute("times", Arrays.toString(times));
		request.setAttribute("values", values);

		return "forward:/web/sort.jsp";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryGoods.do")
	public String queryGoods() {
		try {
			int pageNum = 0;
			String t = request.getParameter("pageNum");
			if (StringUtil.notEmpty(t)) {
				pageNum = Integer.valueOf(t);
			}
			Page p = (Page) session.getAttribute(Constant.SESSION_PAGE + "_WEB");
			if (pageNum == 0 || p == null) {
				p = new Page();
				p.setCurrentPageNumber(1);
				p.setTotalNumber(0l);
				//p.setItemsPerPage(Constant.SESSION_PAGE_NUMBER);
				p.setItemsPerPage(48);

				// 字段名称集合
				LinkedList<String> parmnames = new LinkedList<String>();
				// 字段值集合
				LinkedList<Object> parmvalues = new LinkedList<Object>();
				// 页面参数集合
				Set parm = request.getParameterMap().entrySet();

				for(Object o:parm){
					Entry<String, Object> e = (Entry<String, Object>) o;
					System.out.println(e.getKey());
					System.out.println(e.getValue().toString());
					System.out.println("-----------------------------");
				}

				for (Object o : parm) {
					Entry<String, Object> e = (Entry<String, Object>) o;
					String name = e.getKey();// 页面字段名称
					if (name.startsWith("s_")) {
						String fieldValue = request.getParameter(name);// 页面字段值
						if (StringUtil.notEmpty(fieldValue)) {
							name = name.substring(2, name.length());// 实体字段名称
							parmnames.add(name);
							parmvalues.add(FieldUtil.format(GoodsObject.class, name, fieldValue));
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
			page = service.find(p, GoodsObject.class);

			session.setAttribute(Constant.SESSION_PAGE + "_WEB", page);

			return "forward:/web/list.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
}
