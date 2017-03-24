package com.aspire.webbas.portal.common.authapi;

import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.Staff;

import java.util.List;
import java.util.Map;

/**
 * portlaClient操作接口
 * <pre>
 * <b>Title：</b>PortalClient.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:03:03<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface PortalClient {
	
	/**
	 * 鉴权：根据ticket获取登录用户信息
	 * @param paramString	ticket
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:06:51
	 */
	public abstract Staff auth(String paramString);

	/**
	 * 验证是否有权限操作
	 * @param paramString1	ticket
	 * @param paramString2	url
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:07:36
	 */
	public abstract AuthResult authUrl(String paramString1, String paramString2);

	/**
	 * 根据登录名查询用户信息
	 * @param paramString loginName
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:07:48
	 */
	public abstract Staff findStaffByLoginName(String paramString);

	/**
	 * 根据用户id查询用户信息
	 * @param paramLong staffId
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:08:20
	 */
	public abstract Staff findStaffById(Long paramLong);

	/**
	 * 根据部门id查询部门信息
	 * @param paramLong departmentId
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:08:32
	 */
	public abstract Department findDepartmentById(Long paramLong);

	/**
	 * 根据部门id查询角色列表 
	 * @param paramLong	departmentId
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:08:47
	 */
	public abstract List<Role> listRoleByDepartmentId(Long paramLong);

	/**
	 * 根据用户id查询角色列表
	 * @param paramLong	staffId
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:09:02
	 */
	public abstract List<Role> listStaffRoles(Long paramLong);

	/**
	 * 授权操作
	 * @param paramLong				staffId
	 * @param paramArrayOfString1	String[] resKeys
	 * @param paramArrayOfString2	String[] operKeys
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:09:37
	 */
	public abstract List<Map<String, Boolean>> authorize(Long paramLong, String[] paramArrayOfString1, String[] paramArrayOfString2);

	/**
	 * 根据部门id查询用户列表
	 * @param paramLong	departmentId
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:10:10
	 */
	public abstract List<Staff> listStaffByDepartmentId(Long paramLong) throws Exception;

	/**
	 * 根据部门id及key查询用户列表
	 * @param paramLong		departmentId
	 * @param paramString	keyword
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:10:29
	 */
	public abstract List<Staff> listAllStaffByDepartmentId(Long paramLong, String paramString) throws Exception;

	/**
	 * 根据部门id，key以及domain查询用户列表
	 * @param paramLong		departmentId
	 * @param paramString1	keyword
	 * @param paramString2	domain
	 * @return	
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:10:53
	 */
	public abstract List<Staff> listAllStaffByDepartmentId(Long paramLong, String paramString1, String paramString2) throws Exception;

	/**
	 * 根据domain查询用户列表
	 * @param paramString1
	 * @param paramString2
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:11:11
	 */
	public abstract List<Staff> listStaffsByDomain(String paramString1, String paramString2) throws Exception;

	/**
	 * 根据keyword查询用户列表
	 * @param paramString	keyword
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:11:40
	 */
	public abstract List<Staff> searchStaffs(String paramString) throws Exception;

	/**
	 * 根据keyword和departmentId查询用户列表
	 * @param paramString	keyword
	 * @param paramLong		departmentId
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:12:14
	 */
	public abstract List<Staff> searchStaffs(String paramString, Long paramLong) throws Exception;

	/**
	 * 添加用户
	 * @param paramStaff	Staff
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:14:57
	 */
	public abstract Staff createStaff(Staff paramStaff) throws Exception;

	/**
	 * 根据角色添加用户
	 * @param paramStaff 	staff
	 * @param paramString	roleIds
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:15:18
	 */
	public abstract Staff createStaff(Staff paramStaff, String paramString) throws Exception;

	/**
	 * 添加部门信息
	 * @param paramDepartment	department
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:16:38
	 */
	public abstract void createDepartment(Department paramDepartment) throws Exception;

	/**
	 * 添加部门及角色关联信息
	 * @param paramLong1	departmentId
	 * @param paramLong2	roleId
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:17:40
	 */
	public abstract void insertDepartmentRole(Long paramLong1, Long paramLong2) throws Exception;

	/**
	 * 更新用户关联部门信息
	 * @param paramLong		departmentId
	 * @param paramString	staffIds
	 * @throws Exception	
	 * @author WML
	 * 2016年11月8日 - 下午5:18:21
	 */
	public abstract void updateStaffDepartment(Long paramLong, String paramString) throws Exception;

	/**
	 * 更新用户角色关联部门信息
	 * @param paramLong		departmentId
	 * @param paramString1	staffIdRoles
	 * @param paramString2	staffIdRoles
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:18:55
	 */
	public abstract void updateStaffRolesDepartment(Long paramLong, String paramString1, String paramString2) throws Exception;

	/**
	 * 重置密码
	 * @param paramString1	loginName
	 * @param paramString2	newPassword
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:19:36
	 */
	public abstract void resetPassword(String paramString1, String paramString2) throws Exception;

	/**
	 * 鉴权地址排除
	 * @param paramString	addressUrl
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 下午5:19:55
	 */
	public abstract boolean authExclude(String paramString) throws Exception;
}