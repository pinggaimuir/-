package com.wy.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class ObjectDao extends HibernateDaoSupport {

	// conditionΪ��������ѯһ�����ݣ��÷����ķ�������ΪObject
	public Object getObjectForm(String condition) {
		List list = null;
		Object object = null;
		try {
			list = getHibernateTemplate().find(condition);
			if (list.size() == 1) {
				object = (Object) list.get(0);
			}
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return object;
	}

	// conditionΪ��������ѯ�������ݣ��÷����ķ�������ΪList
	public List getObjectList(String condition) {
		List list = null;
		try {
			list = getHibernateTemplate().find(condition);		
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return list;
	}

	// �޸�һ�����ݣ��÷����ķ�������Ϊboolean
	public boolean updateObjectForm(Object object) {
		boolean flag = false;
		try {
			getHibernateTemplate().update(object);
			flag = true;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	// ���һ�����ݣ��÷����ķ�������Ϊboolean
	public void insertObjectForm(Object object) {
		try {
		
			getHibernateTemplate().save(object);	
			
		} catch (DataAccessException ex) {
			ex.printStackTrace();

		}
		
	}

	// ɾ��һ�����ݣ��÷����ķ�������Ϊboolean
	public boolean deleteObjectForm(Object object) {
		try {
			getHibernateTemplate().delete(object);
			return true;
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
