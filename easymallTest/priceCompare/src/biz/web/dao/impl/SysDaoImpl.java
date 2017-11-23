package biz.web.dao.impl;

import biz.entity.main.User;
import biz.web.dao.ISysDao;
import common.dao.hibernate.BaseHibernateDao;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class SysDaoImpl extends BaseHibernateDao implements ISysDao {
	public void saveUser(User user) {
		save(user);
	}

	public Object queryUser(String type, String userid, String pwd) {
		return unique("from " + type + " where user.uname='" + userid + "' and user.userPassword='" + pwd + "'");
	}

	public User queryUser(String userid) {
		return unique("from User where userid=?", userid);
	}
	public List queryByHQLLimit(final String hql, final int start, final int max) {

		try {

			List resultList = getHibernateTemplate().executeFind(

			new HibernateCallback() {

				public Object doInHibernate(Session arg0)

				throws HibernateException, SQLException {

					Query query = arg0.createQuery(hql);

					query.setFirstResult(start);

					query.setMaxResults(max);

					return query.list();

				}

			});

			return resultList;

		} catch (RuntimeException re) {

			throw re;

		}

	}
	 

}
