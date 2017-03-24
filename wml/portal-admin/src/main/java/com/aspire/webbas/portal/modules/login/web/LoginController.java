package com.aspire.webbas.portal.modules.login.web;

import com.aspire.webbas.core.web.BaseController;
import com.aspire.webbas.portal.common.config.Config;
import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.entity.StaffExtendProperty;
import com.aspire.webbas.portal.common.service.DepartmentService;
import com.aspire.webbas.portal.common.service.LoginService;
import com.aspire.webbas.portal.common.service.StaffService;
import com.aspire.webbas.portal.common.util.CheckCodeUtil;
import com.aspire.webbas.portal.common.util.CookieUtil;
import com.aspire.webbas.portal.common.util.RSAUtil;
import com.aspire.webbas.portal.common.util.StaffUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ��¼Controller
 * <pre>
 * <b>Title��</b>LoginController.java<br/>
 * <b>@author��</b>ynt<br/>
 * <b>@date��</b>2016��11��7�� - ����5:50:24<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
@Controller
@RequestMapping({ "/portal" })
public class LoginController extends BaseController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
	@SuppressWarnings("unused")
	private static final String LOGIN_STAFF_TMP = "LOGIN_STAFF_TMP";

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;

	@Autowired
	@Qualifier("staffService")
	private StaffService staffService;

	@Autowired
	@Qualifier("departmentService")
	private DepartmentService departmentService;

	/**
	 * ��¼�ӿ�
	 * @param request
	 * @param response
	 * @param loginName	��¼��
	 * @param password	����
	 * @param checkCode	��֤��
	 * @param isAuto	�Ƿ��ס����
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:47:36
	 */
	@RequestMapping(value = { "/login.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> login(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam String loginName,
			@RequestParam String password,
			@RequestParam(value = "checkCode", required = false) String checkCode,
			@RequestParam(value = "isAuto", required = false) String isAuto) {
		try {
			if ((isEmpty(loginName)) || (isEmpty(password))) {
				throw new Exception("�û�/����Ϊ��");
			}

			if ((Config.getInstance().isCheckCodeOn()) && (isEmpty(checkCode))) {
				throw new Exception("��֤��Ϊ��");
			}

			if ((Config.getInstance().isCheckCodeOn())
					&& (!CheckCodeUtil.check(getSession(), checkCode))) {
				throw new Exception("��֤�벻��ȷ");
			}

			String pwd = RSAUtil.decryptString(password);

			this.loginService.login(loginName, pwd, getSession());
			CookieUtil.setCookie(request, response, "AUTH_TICKET", getSession().getId());

			/**
			 * ��ס�����cookie
			 */
			if ((isNotEmpty(isAuto)) && ("true".equalsIgnoreCase(isAuto))) {
				CookieUtil.setCookie(request, response, "username", loginName, 259200);
				CookieUtil.setCookie(request, response, "token", password, 259200);
			}

			Map<String, Object> result = new HashMap<String, Object>();
			if (Config.getInstance().isContractAgreementOn()) {
				Staff staff = (Staff) getSession().getAttribute("LOGIN_STAFF");
				Department department = null;
				if (null != staff.getDepartmentId()) {
					department = this.departmentService.findDepartment(staff.getDepartmentId());
					result.put("department", department);
				}
				List<StaffExtendProperty> list = this.staffService.listStaffExtendProperties(staff.getStaffId());
				result.put("staffExtendProperty", list);
				if ((null != department) && ("SP".equals(department.getDomain()))
						&& (!this.loginService.isReadContractAgreement(list))) {
					getSession().setAttribute("LOGIN_STAFF_TMP", staff);
					getSession().removeAttribute("LOGIN_STAFF");
				}
			}
			return success(result);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return fail(e.getMessage());
		}
	}

	/**
	 * �˳��˻�
	 * @param request
	 * @param response
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:52:41
	 */
	@RequestMapping(value = { "/logout.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> logout(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			this.loginService.logout(getSession());
			CookieUtil.delCookie(request, response, "AUTH_TICKET");
			CookieUtil.delCookie(request, response, "username");
			CookieUtil.delCookie(request, response, "token");
			return success("�û��˳��ɹ�");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return fail(e.getMessage());
		}
	}

	/**
	 * ѡ��Э��
	 * @return
	 * @author WML
	 * 2016��11��7�� - ����5:52:53
	 */
	@RequestMapping(value = { "/readContractAgreement.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> readContractAgreement() {
		try {
			Staff staff = (Staff) getSession().getAttribute("LOGIN_STAFF_TMP");

			if (staff == null) {
				throw new Exception("�û�û�е�¼");
			}
			StaffExtendProperty staffExtendProperty = this.loginService.setReadContractAgreement(staff);
			LOG.debug("�û�[" + staff.toString() + "]ͬ���˺���Э��");
			getSession().setAttribute("LOGIN_STAFF", staff);
			getSession().removeAttribute("LOGIN_STAFF_TMP");
			return success(staffExtendProperty);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return fail(e.getMessage());
		}
	}

	@RequestMapping(value = { "/logintest.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> logintest() {
		try {
			Staff staff = StaffUtil.getLoginStaff();

			if (staff == null) {
				throw new Exception("�û�û�е�¼");
			}

			return success("�û�[" + staff.getLoginName() + "]�Ѿ���¼");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return fail(e.getMessage());
		}
	}
}