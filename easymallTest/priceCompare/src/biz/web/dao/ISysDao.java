package biz.web.dao;

import java.util.List;

import biz.entity.main.User;
import common.dao.hibernate.BaseDao;

public interface ISysDao extends BaseDao{
	public void saveUser(User user);

	public Object queryUser(String type, String userid, String pwd) ;

	public User queryUser(String userid) ;

	public List queryByHQLLimit(String hql, int start, int max);
	public <T> T unique(final String hql, final Object... paramlist);
}
