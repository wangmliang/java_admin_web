package com.aspire.webbas.portal.common.util;

import com.aspire.webbas.portal.common.auth.session.SessionContext;
import com.aspire.webbas.portal.common.entity.Staff;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class StaffUtil {
	private static HttpServletRequest getRequest() {
		if (null != RequestContextHolder.getRequestAttributes()) {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		}
		return null;
	}

	private static HttpSession getSession() {
		return getRequest().getSession(true);
	}

	public static String getLoginUserName() throws Exception {
		Staff staff = getLoginStaff();
		if (null != staff) {
			return staff.getLoginName();
		}
		return "";
	}

	public static Staff getLoginStaff() throws Exception {
		HttpSession session = SessionContext.getContext().getSession(getSession().getId());

		if (session == null) {
			throw new Exception("用户没有登录");
		}

		Staff staff = (Staff) session.getAttribute("LOGIN_STAFF");

		if (null == staff) {
			throw new Exception("用户没有登录");
		}
		return staff;
	}

	public static void addLoginSatff(HttpSession session, Staff newStaff) {
		HttpSession se = SessionContext.getContext().getSession(getSession().getId());

		if (se == null) {
			session.setAttribute("LOGIN_STAFF", newStaff);
			SessionContext.getContext().addSession(session);
		} else {
			se.setAttribute("LOGIN_STAFF", newStaff);
		}
	}

	public static void updateLoginStaff(Staff newStaff) {
		HttpSession session = SessionContext.getContext().getSession(getSession().getId());

		if (session == null) {
			return;
		}
		Staff staff = (Staff) session.getAttribute("LOGIN_STAFF");

		if ((staff != null) && (newStaff != null) && (staff.getStaffId().equals(newStaff.getStaffId())))
			getSession().setAttribute("LOGIN_STAFF", newStaff);
	}
}