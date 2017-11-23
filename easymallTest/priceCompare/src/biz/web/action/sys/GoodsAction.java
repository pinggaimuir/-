package biz.web.action.sys;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import util.Constant;
import util.DateUtil;
import util.FieldUtil;
import util.MessageUtil;
import util.Page;
import util.SearchParamBean;
import util.StringUtil;
import biz.entity.main.GoodsObject;
import biz.entity.main.GoodsPrice;
import biz.web.action.BaseAction;
import biz.web.fetch.HtmlData;
import biz.web.fetch.ObjectFormatManager;
import biz.web.service.impl.BizService;

@Controller
@RequestMapping("/sys")
public class GoodsAction extends BaseAction {
	@Autowired
	private BizService service;

	@RequestMapping(value = "/add2GoodsObject.do", method = RequestMethod.GET)
	public String add2() {

		//		List<GoodsObject> list = service.findAll(GoodsObject.class);

		//		Date cdate = new Date();
		//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		//		Random random = new Random();
		//		for (GoodsObject goods : list) {
		//			GoodsPrice pricebean = service.getPrice(goods);
		//			Double temp = (Double) (pricebean.getPrice() * 0.1);
		//			int ranprice = temp.intValue();
		//			for (int i = 1; i <= 7; i++) {
		//				Date d = DateUtil.addDays(cdate, 0 - i);
		//				GoodsPrice gp = new GoodsPrice();
		//				gp.setAddDate(sf.format(d));
		//				gp.setGoods(goods);
		//
		//				int bp = random.nextInt(ranprice);
		//				if (bp % 2 == 0) {
		//					gp.setPrice(pricebean.getPrice() + bp);
		//				} else {
		//					gp.setPrice(pricebean.getPrice() - bp);
		//				}
		//				service.add(gp);
		//			}
		//
		//		}

		return "sys/addGoodsObject";
	}

	@RequestMapping(value = "/getGoodsObject.do", method = RequestMethod.GET)
	public String get(int uid) {
		try {
			GoodsObject bean = (GoodsObject) service.get(GoodsObject.class, uid);
			request.setAttribute("modifybean", bean);

			return "sys/modifyGoodsObject";
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addMessage(request, "获取信息失败.");
			return ERROR;
		}
	}

	@RequestMapping(value = "/deleteGoodsObject.do")
	public String delete(String ids) {
		try {
			service.delete(GoodsObject.class, ids);
			MessageUtil.addRelMessage(request, "删除信息成功.", "mainquery");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addMessage(request, "删除信息失败.");
			return ERROR;
		}
	}

	@RequestMapping(value = "/addGoodsObject.do")
	public String add(String key) {
		try {
			final String tempkey = key.trim();
			new Thread(new Runnable() {
				@Override
				public void run() {
					HtmlData queryHtmlUtil = new HtmlData();

					PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
					HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).build();
					int type = ObjectFormatManager.ALL;
					queryHtmlUtil.execute(httpClient, type, tempkey);

				}
			}).start();
			MessageUtil.addMessage(request, "爬虫线程已经开启.爬取到的商品将添加或者更新到数据库");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addMessage(request, "爬虫线程已经开启失败.");
			return ERROR;
		}

	}

	@RequestMapping(value = "/updateGoodsObject.do")
	public String update(GoodsObject bean) {
		try {
			service.update(bean);
			MessageUtil.addMessage(request, "更新成功.");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.addMessage(request, "更新失败.");
			return ERROR;
		}
	}

	@RequestMapping(value = "/queryGoodsObject.do")
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

			session.setAttribute(Constant.SESSION_PAGE, page);
			return "sys/listGoodsObject";
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

}
