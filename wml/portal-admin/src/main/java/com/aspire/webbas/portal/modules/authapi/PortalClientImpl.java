package com.aspire.webbas.portal.modules.authapi;

import com.aspire.webbas.core.exception.MyException;
import com.aspire.webbas.core.util.SpringContextHolder;
import com.aspire.webbas.core.util.StringTools;
import com.aspire.webbas.portal.common.auth.session.SessionContext;
import com.aspire.webbas.portal.common.authapi.AuthResult;
import com.aspire.webbas.portal.common.authapi.PortalClient;
import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.service.AuthService;
import com.aspire.webbas.portal.common.service.DepartmentService;
import com.aspire.webbas.portal.common.service.RoleService;
import com.aspire.webbas.portal.common.service.StaffService;
import com.aspire.webbas.portal.common.service.impl.AuthServiceImpl;
import com.aspire.webbas.portal.common.service.impl.DepartmentServiceImpl;
import com.aspire.webbas.portal.common.service.impl.RoleServiceImpl;
import com.aspire.webbas.portal.common.service.impl.StaffServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * portalClient实现类
 * <pre>
 * <b>Title：</b>PortalClientImpl.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 上午9:13:15<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class PortalClientImpl implements PortalClient {
	private static String SUCCESS = "0";
	private static String FAIL = "1";
	private static String INVALID = "2";
	private static String NOT_LOGIN = "3";

	private static Logger logger = LoggerFactory.getLogger(PortalClientImpl.class);

	/**
	 * 鉴权：根据ticket获取登录用户信息
	 */
	public Staff auth(String ticket) {
		HttpSession session = SessionContext.getContext().getSession(ticket);
		if (session == null) {
			return null;
		}
		Staff staff = (Staff) session.getAttribute(SessionContext.LOGIN_STAFF);
		return staff;
	}

	/**
	 * 验证是否有权限操作
	 */
	public AuthResult authUrl(String ticket, String url) {
		try {
			HttpSession session = SessionContext.getContext().getSession(ticket);

			if (session == null) {
				return authFail(INVALID, "ticket无效", "/pages/login.shtml");
			}

			AuthService authService = (AuthService) SpringContextHolder.getBean(AuthServiceImpl.class);

			if (authService.authExclude(url)) {
				return authSuccess();
			}

			Staff staff = (Staff) session.getAttribute(SessionContext.LOGIN_STAFF);

			if (staff == null) {
				return authFail(NOT_LOGIN, "用户未登录", "/pages/login.shtml");
			}

			if (authService.notNeedAuthAfterLogin(url)) {
				return authSuccess();
			}

			if (authService.authorize(staff.getStaffId(), url)) {
				return authSuccess();
			}
			return authFail(FAIL, "鉴权失败", "/pages/noAuthority.shtml");
		} catch (Exception e) {
			return authFail(FAIL, e.getMessage(), "/pages/noAuthority.shtml");
		}
	}

	/**
	 * 根据登录名查询用户信息
	 */
	public Staff findStaffByLoginName(String loginName) {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		try {
			return staffService.findStaffByLoginName(loginName);
		} catch (Exception e) {
			logger.error("根据loginName=" + loginName + ",获取用户信息出错", e);
		}

		return null;
	}

	/**
	 * 根据用户id查询用户信息
	 */
	public Staff findStaffById(Long staffId) {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		try {
			return staffService.findStaff(staffId);
		} catch (Exception e) {
			logger.error("根据staffId=" + staffId + ",获取用户信息出错", e);
		}

		return null;
	}

	/**
	 * 根据部门id查询部门信息
	 */
	public Department findDepartmentById(Long departmentId) {
		DepartmentService deptService = (DepartmentService) SpringContextHolder.getBean(DepartmentServiceImpl.class);
		try {
			return deptService.findDepartment(departmentId);
		} catch (Exception e) {
			logger.error("根据departmentId=" + departmentId + ",获取部门信息出错", e);
		}

		return null;
	}

	/**
	 * 根据部门id查询角色列表
	 */
	public List<Role> listRoleByDepartmentId(Long departmentId) {
		RoleService roleService = (RoleService) SpringContextHolder.getBean(RoleServiceImpl.class);
		try {
			return roleService.listDepartmentRoles(departmentId);
		} catch (Exception e) {
			logger.error("根据departmentId=" + departmentId + ",获取角色列表出错", e);
		}
		return new ArrayList<Role>();
	}

	/**
	 * 根据用户id查询角色列表
	 */
	public List<Role> listStaffRoles(Long staffId) {
		RoleService roleService = (RoleService) SpringContextHolder.getBean(RoleServiceImpl.class);
		try {
			return roleService.listStaffRoles(staffId);
		} catch (Exception e) {
			logger.error("根据staffId=" + staffId + ",查询指定员工被赋予了那些角色出错", e);
		}
		return new ArrayList<Role>();
	}

	/**
	 * 鉴权失败。返回code-message-redirectUrl
	 * @param code
	 * @param message
	 * @param url
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午9:16:28
	 */
	private AuthResult authFail(String code, String message, String url) {
		AuthResult result = new AuthResult();
		result.setReturnCode(code);
		result.setMessage(message);
		result.setRedirectUrl(url);
		return result;
	}

	/**
	 * 鉴权成功
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午9:17:24
	 */
	private AuthResult authSuccess() {
		AuthResult result = new AuthResult();
		result.setReturnCode(SUCCESS);
		result.setMessage("鉴权成功");
		return result;
	}

	/**
	 * 授权
	 */
	public List<Map<String, Boolean>> authorize(Long staffId, String[] resKeys, String[] operKeys) {
		AuthService authService = (AuthService) SpringContextHolder.getBean(AuthServiceImpl.class);
		return authService.authorize(staffId, resKeys, operKeys);
	}

	/**
	 * 根据部门id查询用户列表
	 */
	public List<Staff> listStaffByDepartmentId(Long departmentId) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		return staffService.listDepartmentStaffs(departmentId);
	}

	/**
	 * 根据部门id及key查询用户列表
	 */
	public List<Staff> listAllStaffByDepartmentId(Long departmentId, String keyword) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		return staffService.listDepartmentAllStaffs(departmentId, keyword, null);
	}

	/**
	 * 根据部门id，key以及domain查询用户列表
	 */
	public List<Staff> listAllStaffByDepartmentId(Long departmentId, String keyword, String domain) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		return staffService.listDepartmentAllStaffs(departmentId, keyword,domain);
	}

	/**
	 * 根据domain查询用户列表
	 */
	public List<Staff> listStaffsByDomain(String domain, String keyword) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		return staffService.listStaffsByDomain(domain, keyword);
	}

	/**
	 * 根据keyword查询用户列表
	 */
	public List<Staff> searchStaffs(String keyword) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		return staffService.listStaffs(null, keyword);
	}

	/**
	 * 根据keyword和departmentId查询用户列表
	 */
	public List<Staff> searchStaffs(String keyword, Long departmentId) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		return staffService.listStaffs(departmentId, keyword);
	}

	/**
	 *  添加用户
	 */
	public Staff createStaff(Staff staff) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		staffService.createStaff(staff);
		return staff;
	}

	/**
	 * 更新用户
	 * @param staff
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:15:58
	 */
	public void updateStaff(Staff staff) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		staffService.updateStaff(staff);
	}

	/**
	 * 根据角色添加用户
	 */
	public Staff createStaff(Staff staff, String roleIds) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		staffService.createStaff(staff, roleIds);
		return staff;
	}

	/**
	 * 添加部门信息
	 */
	public void createDepartment(Department department) throws Exception {
		DepartmentService departmentService = (DepartmentService) SpringContextHolder.getBean(DepartmentServiceImpl.class);
		departmentService.insertDepartment(department);
	}

	/**
	 * 添加部门及角色关联信息
	 */
	public void insertDepartmentRole(Long departmentId, Long roleId) throws Exception {
		DepartmentService departmentService = (DepartmentService) SpringContextHolder.getBean(DepartmentServiceImpl.class);
		departmentService.insertDepartmentRole(departmentId, roleId);
	}

	/**
	 * 更新用户关联部门信息
	 */
	public void updateStaffDepartment(Long departmentId, String staffIds) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		staffService.updateStaffDepartment(departmentId, staffIds);
	}

	/**
	 * 更新用户角色关联部门信息
	 */
	public void updateStaffRolesDepartment(Long departmentId, String staffIds, String staffIdRoles) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		staffService.updateStaffRolesDepartment(departmentId, staffIds, staffIdRoles);
	}

	/**
	 * 重置密码
	 */
	public void resetPassword(String loginName, String newPassword) throws Exception {
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		staffService.resetPassword(loginName, newPassword);
	}

	/**
	 * 鉴权地址排除
	 */
	public boolean authExclude(String addressUrl) throws Exception {
		AuthService authService = (AuthService) SpringContextHolder.getBean(AuthServiceImpl.class);
		return authService.authExclude(addressUrl);
	}

	/**
	 * 根据登录名删除用户
	 * @param loginName
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:20:12
	 */
	public void deleteStaffByLoginName(String loginName) throws Exception {
		if (StringTools.isEmptyString(loginName)) {
			throw new MyException("参数不能为空");
		}
		StaffService staffService = (StaffService) SpringContextHolder.getBean(StaffServiceImpl.class);
		Staff staff = staffService.findStaffByLoginName(loginName);
		if (null == staff) {
			throw new MyException("通过LoginName[" + loginName + "]查找不到要删除的用户");
		}
		staffService.deleteStaffs(new Long[] { staff.getStaffId() });
	}
}