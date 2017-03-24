package com.aspire.webbas.portal.modules.home.web;

import com.aspire.webbas.core.web.BaseController;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.service.MenuService;
import com.aspire.webbas.portal.common.tree.MenuTreeNode;
import com.aspire.webbas.portal.common.util.StaffUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * �˵�Controller
 * <pre>
 * <b>Title��</b>MenuController.java<br/>
 * <b>@author��</b>WML<br/>
 * <b>@date��</b>2016��11��7�� - ����5:54:21<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
@Controller
@RequestMapping({ "/portal" })
public class MenuController extends BaseController {

	@Autowired
	@Qualifier("menuService")
	private MenuService menuService;

	/**
	 * �����û���ȡ��Ӧ�Ĳ˵���
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:54:30
	 */
	@RequestMapping({ "/menu.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> menu() {
		try {
			Staff loginStaff = StaffUtil.getLoginStaff();

			if (loginStaff == null) {
				throw new Exception("�û�Ϊ��¼");
			}
			HttpSession session = getSession();
			List<MenuTreeNode> menuTree = this.menuService.buildMenuTree(loginStaff.getLoginName(), session.getId(), getRequest().getContextPath());
			return success(menuTree);
		} catch (Exception e) {
			return fail(e.getMessage());
		}
	}
}