package com.wy.action;

import com.wy.dao.ObjectDao;
import com.wy.form.ManagerForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ManagerAction extends DispatchAction {
	private ObjectDao objectDao;

	public ObjectDao getObjectDao() {
		return objectDao;
	}

	public void setObjectDao(ObjectDao objectDao) {
		this.objectDao = objectDao;
	}

	// �û���¼����
	public ActionForward checkManager(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ManagerForm managerForm = (ManagerForm) form;
		HttpSession session = request.getSession();
		ManagerForm managerform = (ManagerForm) objectDao
				.getObjectForm("from ManagerForm where account='"
						+ managerForm.getAccount() + "'");
		if (managerform == null) {
			request.setAttribute("errorNews", "��������˺Ų�����");
		} else if (!managerform.getPassword().equals(managerForm.getPassword())) {
			request.setAttribute("errorNews", "����������벻��ȷ");
		} else {
			session.setAttribute("managerform", managerform);
		}
		return mapping.findForward("checkManager");
	}

	// �û��޸Ĳ���
	public ActionForward updateManager(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ManagerForm managerForm = (ManagerForm) form;
		String result = "�û��޸�ʧ�ܣ�����";
		if (objectDao.updateObjectForm(managerForm))
			result = "�û��޸ĳɹ�������";
		request.setAttribute("result", result);
		return mapping.findForward("operationManager");
	}

	// �û��쿴����
	public ActionForward queryManager(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List list = objectDao
				.getObjectList("from ManagerForm where managerLevel!=1");
		request.setAttribute("list", list);
		return mapping.findForward("queryManager");
	}

	// �û��������
	public ActionForward insertManager(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ManagerForm managerForm = (ManagerForm) form;
		ManagerForm managerform = (ManagerForm) objectDao
				.getObjectForm("from ManagerForm where account='"
						+ managerForm.getAccount() + "'");
		if (managerform == null) {
			objectDao.insertObjectForm(managerForm);
			return queryManager(mapping, form, request, response);
		} else {
			request.setAttribute("result", "��������˺��ظ�������������");
			return mapping.findForward("errorManager");
		}
	}

	// �û�ɾ������
	public ActionForward deleteManager(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {		
		ManagerForm managerform = (ManagerForm) objectDao.getObjectForm("from ManagerForm where id='"
						+ request.getParameter("id") + "'");		
		objectDao.deleteObjectForm(managerform);
		return queryManager(mapping, form, request, response);		
	}

}
