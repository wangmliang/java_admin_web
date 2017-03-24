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
 * 菜单Controller
 * <pre>
 * <b>Title：</b>MenuController.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月7日 - 下午5:54:21<br/>  
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
	 * 根据用户获取对应的菜单项
	 * @return
	 * @author WML
	 * 2016年11月7日 - 下午5:54:30
	 */
	@RequestMapping({ "/menu.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> menu() {
		try {
			Staff loginStaff = StaffUtil.getLoginStaff();

			if (loginStaff == null) {
				throw new Exception("用户为登录");
			}
			HttpSession session = getSession();
			List<MenuTreeNode> menuTree = this.menuService.buildMenuTree(loginStaff.getLoginName(), session.getId(), getRequest().getContextPath());
			return success(menuTree);
		} catch (Exception e) {
			return fail(e.getMessage());
		}
	}
}