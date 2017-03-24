package com.aspire.webbas.portal.common.servlet;

import com.aspire.webbas.core.servlet.BaseServlet;
import com.aspire.webbas.core.util.SpringContextHolder;
import com.aspire.webbas.core.util.StringTools;
import com.aspire.webbas.portal.common.auth.session.SessionContext;
import com.aspire.webbas.portal.common.config.Config;
import com.aspire.webbas.portal.common.entity.City;
import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.entity.StaffExtendProperty;
import com.aspire.webbas.portal.common.service.CityService;
import com.aspire.webbas.portal.common.service.DepartmentService;
import com.aspire.webbas.portal.common.service.LoginService;
import com.aspire.webbas.portal.common.service.StaffService;
import com.aspire.webbas.portal.common.service.impl.DepartmentServiceImpl;
import com.aspire.webbas.portal.common.service.impl.LoginServiceImpl;
import com.aspire.webbas.portal.common.service.impl.StaffServiceImpl;
import com.aspire.webbas.portal.common.util.CookieUtil;
import com.aspire.webbas.portal.common.util.RSAUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacadeServlet extends BaseServlet {
	private static final long serialVersionUID = 2416289388042304446L;
	private static final Logger LOG = LoggerFactory
			.getLogger(FacadeServlet.class);
	private LoginService loginService;
	private StaffService staffService;
	private DepartmentService departmentService;

	private boolean autoLogin(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = SessionContext.getContext().getSession(request.getSession(true).getId());
		if ((null != session) && (null != session.getAttribute("LOGIN_STAFF"))) {
			return false;
		}
		String username = CookieUtil.getCookie(request, "username");
		String password = CookieUtil.getCookie(request, "token");
		String passwd = RSAUtil.decryptString(password);
		if ((StringTools.isNotEmptyString(username)) && (StringTools.isNotEmptyString(passwd))) {
			this.loginService = ((LoginService) SpringContextHolder.getBean(LoginServiceImpl.class));
			try {
				if (Config.getInstance().isContractAgreementOn()) {
					this.staffService = ((StaffService) SpringContextHolder.getBean(StaffServiceImpl.class));
					Staff staff = this.staffService.findStaffByLoginName(username);
					Department department = null;
					if (null != staff.getDepartmentId()) {
						this.departmentService = ((DepartmentService) SpringContextHolder.getBean(DepartmentServiceImpl.class));
						department = this.departmentService.findDepartment(staff.getDepartmentId());
					}
					List<StaffExtendProperty> list = this.staffService
							.listStaffExtendProperties(staff.getStaffId());
					if ((null != department)
							&& ("SP".equals(department.getDomain()))
							&& (!this.loginService
									.isReadContractAgreement(list))) {
						throw new RuntimeException("由于用户未同意协议，自动登录无效，此异常可忽略。");
					}
				}
				this.loginService.login(username, passwd, request.getSession());
				CookieUtil.setCookie(request, response, "AUTH_TICKET", request
						.getSession().getId());

				CookieUtil.setCookie(request, response, "username", username,
						259200);

				CookieUtil.setCookie(request, response, "token", password,
						259200);

				return true;
			} catch (Exception e) {
				LOG.error("在Filter中自动登录失败，错误可忽略", e);
				try {
					CookieUtil.delCookie(request, response, "username");
					CookieUtil.delCookie(request, response, "token");
				} catch (ServletException e1) {
					LOG.error("在Filter中删除自动登录cookie出错，错误可忽略", e1);
				}
			}
		}

		return false;
	}

	public void getLoginInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String title = Config.getInstance().getTitle();
		String modulus = RSAUtil.bigIntToHexStr(RSAUtil.getDefaultPublicKey()
				.getModulus());
		String exponent = RSAUtil.bigIntToHexStr(RSAUtil.getDefaultPublicKey()
				.getPublicExponent());
		Staff loginStaff = null;
		autoLogin(request, response);
		HttpSession session = SessionContext.getContext().getSession(
				request.getSession(true).getId());
		if (null != session) {
			loginStaff = (Staff) session.getAttribute("LOGIN_STAFF");
		}
		JSONObject config = new JSONObject();
		config.put("title", title);
		config.put("modulus", modulus);
		config.put("exponent", exponent);
		config.put("isLogin", Boolean.valueOf(null != loginStaff));
		config.put("isCheckcodeOn",
				Boolean.valueOf(Config.getInstance().isCheckCodeOn()));
		config.put("isRegisterOn",
				Boolean.valueOf(Config.getInstance().isRegisterOn()));
		config.put("registerUrl", Config.getInstance().getRegisterUrl());
		config.put("isForgotpwdOn",
				Boolean.valueOf(Config.getInstance().isForgotpwdOn()));
		config.put("forgotpwdUrl", Config.getInstance().getForgotpwdUrl());
		config.put("isContractAgreementOn",
				Boolean.valueOf(Config.getInstance().isContractAgreementOn()));

		super.outputJS(request, response, "var config = " + config.toString());
	}

	public void getStaffInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String title = Config.getInstance().getTitle();
		autoLogin(request, response);
		HttpSession session = SessionContext.getContext().getSession(
				request.getSession().getId());
		Staff loginStaff = (Staff) session.getAttribute("LOGIN_STAFF");
		String realName = "";
		if (null != loginStaff) {
			realName = loginStaff.getRealName();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("window.title='" + title + "';");
		sb.append("var realName='" + realName + "';");
		super.outputJS(request, response, sb.toString());
	}

	public void listCity(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		CityService cityService = (CityService) SpringContextHolder
				.getBean(CityService.class);
		List<City> list = cityService.list(null);
		StringBuilder sb = new StringBuilder();
		sb.append("var citys=" + JSONArray.fromObject(list).toString() + ";");
		super.outputJS(request, response, sb.toString());
	}
}