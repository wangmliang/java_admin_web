package com.demo.admin.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aspire.webbas.client.AuthStaffUtil;
import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Staff;

/**
 * 用户工具类
 * <pre>
 * <b>Title：</b>StaffUtil.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月27日 下午4:44:40<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class StaffUtil {

	public static HttpServletRequest getRequest() {
		if (null != RequestContextHolder.getRequestAttributes()) {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} else {
			return null;
		}
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 获取当前登录用户信息
	 * @return
	 * @throws Exception
	 */
	public static Staff getLoginStaff() throws Exception {
		Staff staff = AuthStaffUtil.getLoginStaff(getRequest());
		if (null == staff){
			throw new Exception("用户未登录");
		}
		return staff;
	}

	/**
	 * 获取当前登录用户loginName
	 * @return
	 * @throws Exception
	 */
	public static String getLoginName() throws Exception {
		Staff staff = getLoginStaff();
		if (null == staff)
			return null;
		return staff.getLoginName();
	}

	/**
	 * 获取当前登录用户staffId
	 * @return
	 * @throws Exception
	 */
	public static String getStaffId() throws Exception {
		Staff staff = getLoginStaff();
		if (null == staff)
			return null;
		return "" + staff.getStaffId();
	}

	/**
	 * 是否为管理员
	 * @return true 是 false 否
	 *         如果staff.getDepartment()为空为管理员，或staff.getDepartment(
	 *         ).getDomain()为SYS_ADMIN为管理员
	 * @throws Exception
	 */
	public static boolean isAdmin() throws Exception {
		Staff staff = getLoginStaff();
		if (null == staff) {
			return false;
		}
		Department department = staff.getDepartment();
		if (null == department) {
			return true;
		}
		String domain = department.getDomain();
		if (null != domain && ("SYS_ADMIN").equals(domain)) {
			return true;
		}
		return false;
	}

}
