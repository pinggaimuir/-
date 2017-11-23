package biz.web.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import util.DateUtil;
import util.MD5;
import util.Page;
import biz.entity.main.GoodsObject;
import biz.entity.main.GoodsPrice;
import biz.entity.main.SimpleUser;
import biz.entity.main.SysUser;
import biz.entity.main.User;
import biz.web.dao.ISysDao;
import biz.web.fetch.HtmlData;
import biz.web.fetch.ObjectFormatManager;
import common.service.BaseService;

@Service("bizService")
@Repository
public class BizService extends BaseService {

	@Autowired
	private ISysDao dao;

	public void addNewSearchGoods(String key) {
		try {
			HtmlData queryHtmlUtil = new HtmlData();

			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
			HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).build();
			int type = ObjectFormatManager.ALL;
			queryHtmlUtil.execute(httpClient, type, key);

			List<GoodsObject> tblist = queryHtmlUtil.getTblist();
			List<GoodsObject> jdlist = queryHtmlUtil.getJdlist();

			for (GoodsObject goods : tblist) {

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public List queryByHQL(String hql, Object... values) {
		return dao.queryByHQL(hql, values);
	}

	public void addSimpleUser(SimpleUser obj) {
		User user = obj.getUser();
		MD5 md = new MD5();
		user.setUserPassword(md.getMD5ofStr(user.getUserPassword()));
		dao.save(user);
		dao.save(obj);
	}

	public void updateSimpleUser(SimpleUser obj) {
		SimpleUser temp = (SimpleUser) dao.get(SimpleUser.class, obj.getId());
		User user = temp.getUser();
		user.setUserAddress(obj.getUser().getUserAddress());
		user.setUserBirth(obj.getUser().getUserBirth());
		user.setUserEmail(obj.getUser().getUserEmail());
		user.setUserGender(obj.getUser().getUserGender());
		user.setUserName(obj.getUser().getUserName());
		user.setUserPhone(obj.getUser().getUserPhone());
		if (StringUtils.isNotBlank(obj.getUser().getUserPassword())) {
			MD5 md = new MD5();
			user.setUserPassword(md.getMD5ofStr(obj.getUser().getUserPassword()));
		}
		dao.merge(user);
		obj.setUser(user);
		dao.merge(obj);

	}

	/**
	 * 添加对象
	 * 
	 * @param obj
	 */
	public void add(Object obj) {
		dao.save(obj);
	}

	/**
	 * 修改对象
	 * 
	 * @param obj
	 */
	public void update(Object obj) {
		dao.merge(obj);
	}

	/**
	 * 根据id获取对象
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object get(Class clazz, Serializable id) {
		return dao.get(clazz, id);
	}

	public void deleteUser(String ids) {
		dao.deleteByIds(User.class, ids);
	}

	public void delete(Class clazz, String ids) {
		dao.deleteByIds(clazz, ids);
	}

	public Object findUser(String type, String userid, String pwd) {
		return dao.queryUser(type, userid, pwd);
	}

	public User findUser(String userid) {
		return dao.queryUser(userid);
	}

	public Page findUser(Page page) {
		return dao.list(page, "User");
	}

	public Page find(Page page, Class clazz) {
		return dao.list(page, clazz.getSimpleName());
	}

	public List findAll(Class clazz) {
		return dao.queryByHQL("from " + clazz.getSimpleName());
	}

	public void addSysUser(SysUser obj) {
		User user = obj.getUser();
		MD5 md = new MD5();
		user.setUserPassword(md.getMD5ofStr(user.getUserPassword()));
		dao.save(user);
		dao.save(obj);
	}

	public void updateSysUser(SysUser obj) {
		SysUser temp = (SysUser) dao.get(SysUser.class, obj.getId());
		User user = temp.getUser();
		user.setUserAddress(obj.getUser().getUserAddress());
		user.setUserBirth(obj.getUser().getUserBirth());
		user.setUserEmail(obj.getUser().getUserEmail());
		user.setUserGender(obj.getUser().getUserGender());
		user.setUserName(obj.getUser().getUserName());
		user.setUserPhone(obj.getUser().getUserPhone());
		if (StringUtils.isNotBlank(obj.getUser().getUserPassword())) {
			MD5 md = new MD5();
			user.setUserPassword(md.getMD5ofStr(obj.getUser().getUserPassword()));
		}
		dao.merge(user);
		obj.setUser(user);
		dao.merge(obj);

	}

	public List findAll(Class clazz, Map<String, Object> params) {
		return dao.findAll(clazz, params);
	}

	private static String lock = "addGoodsGoodsObjectgoods";

	public void addGoods(GoodsObject goods) {
		synchronized (lock) {
			GoodsObject temp = dao.unique("from GoodsObject where goodsUrl=?", goods.getGoodsUrl());
			if (temp == null) {
				dao.save(goods);
				GoodsPrice gp = new GoodsPrice();
				gp.setAddDate(DateUtil.getCurrentTime());
				gp.setGoods(goods);
				gp.setPrice(goods.getPrice());
				dao.save(gp);
			} else {
				temp.setGoodsName(goods.getGoodsName());
				temp.setGoodsUrl(goods.getGoodsUrl());
				temp.setPicUrl(goods.getPicUrl());
				temp.setPrice(goods.getPrice());
				dao.update(temp);

				String date = DateUtil.getCurrentTime();

				GoodsPrice gp = dao.unique("from GoodsPrice where goods.id=? and addDate=?", temp.getId(), date);
				if (gp == null) {
					gp = new GoodsPrice();
					gp.setAddDate(date);
					gp.setGoods(temp);
					gp.setPrice(goods.getPrice());
					dao.save(gp);
				} else {
					gp.setPrice(goods.getPrice());
					dao.update(gp);
				}

			}
		}
	}

	public GoodsPrice getPrice(GoodsObject goods) {
		return dao.unique("from GoodsPrice where goods.id=? and addDate=?", goods.getId(), DateUtil.getCurrentTime());
	}
	public GoodsPrice getPrice(GoodsObject goods,String date) {
		return dao.unique("from GoodsPrice where goods.id=? and addDate=?", goods.getId(), date);
	}

	public List<GoodsPrice> findGoodsPriceList(int uid) {
		return dao.queryByHQLLimit("from GoodsPrice where goods.id=" + uid + " order by addDate desc", 0, 7);
	}

}
