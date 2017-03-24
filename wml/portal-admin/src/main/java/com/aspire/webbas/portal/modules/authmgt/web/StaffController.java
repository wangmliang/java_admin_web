package com.aspire.webbas.portal.modules.authmgt.web;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.core.web.BaseController;
import com.aspire.webbas.portal.common.config.StaffExtendPropertyConfig;
import com.aspire.webbas.portal.common.config.StaffField;
import com.aspire.webbas.portal.common.entity.City;
import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.service.CityService;
import com.aspire.webbas.portal.common.service.DepartmentService;
import com.aspire.webbas.portal.common.service.RoleService;
import com.aspire.webbas.portal.common.service.StaffService;
import com.aspire.webbas.portal.common.util.RSAUtil;
import com.aspire.webbas.portal.common.util.StaffUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 员工Controller
 * <pre>
 * <b>Title：</b>StaffController.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 上午8:55:43<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
@Controller
@RequestMapping({ "/staff" })
public class StaffController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

	@Autowired
	@Qualifier("staffService")
	private StaffService staffService;

	@Autowired
	@Qualifier("departmentService")
	private DepartmentService departmentService;

	@Autowired
	@Qualifier("roleService")
	private RoleService roleService;

	@Autowired
	@Qualifier("cityService")
	private CityService cityService;

	/**
	 * 添加员工信息
	 * @param staff
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 上午8:55:56
	 */
	@RequestMapping(value = { "/createStaff.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> createStaff(Staff staff)
			throws Exception {
		try {
			if (isNotEmpty(staff.getStaffId())) {
				staff.setLastUpdateDate(new Date());
				this.staffService.updateStaff(staff);
			} else {
				if (staff.getStatus() == null) {
					staff.setStatus(Staff.Status.INITIAL);
				}
				if (staff.getCreateUser() == null) {
					if (StaffUtil.getLoginStaff() == null) {
						staff.setCreateUser("nouser");
					} else {
						staff.setCreateUser(StaffUtil.getLoginStaff().getLoginName());
					}
				}
				this.staffService.createStaff(staff);
			}
		} catch (Exception e) {
			logger.error("新增用户信息出错", e);
			return fail(e.getMessage());
		}
		return success("创建用户成功。");
	}

	/**
	 * 根据id查询员工信息
	 * @param staffId
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午8:56:14
	 */
	@RequestMapping(value = { "/findStaff.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> findStaff(Long staffId) {
		try {
			if (isEmpty(staffId)) {
				throw new Exception("用户Id参数不能为空");
			}
			Staff staff = this.staffService.findStaff(staffId);
			if (staff == null) {
				throw new Exception(new StringBuilder().append("没有找到用户[id=").append(staffId).append("]").toString());
			}
			staff.setOthers(packStaffOthers(staff));
			return success(staff);
		} catch (Exception e) {
			logger.error(new StringBuilder().append("根据staffId=").append(staffId).append(",查询用户信息出错").toString(), e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 查询员工资源
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午8:56:45
	 */
	@RequestMapping({ "/findStaffExtendProperties.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> findStaffExtendProperties() {
		try {
			List<StaffField> staffFields = new ArrayList<StaffField>();
			if (StaffExtendPropertyConfig.getInstance().isStaffExtendPropertyOn()) {
				staffFields = StaffExtendPropertyConfig.getInstance().getStaffFileds();
			}
			return success(staffFields);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 删除用户
	 * @param staffId
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午8:58:08
	 */
	@RequestMapping(value = { "/deleteStaff.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> deleteStaff(Long staffId) {
		try {
			if (isEmpty(staffId)) {
				throw new Exception("staffId参数为空");
			}
			Long[] staffs = { staffId };
			this.staffService.deleteStaffs(staffs);
		} catch (Exception e) {
			logger.error(new StringBuilder().append("根据staffId=").append(staffId).append(",删除用户信息出错").toString(), e);
			return fail(e.getMessage());
		}
		return success("删除用户成功。");
	}

	/**
	 * 员工列表
	 * @param page
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午9:01:26
	 */
	@RequestMapping({ "/listStaff.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> listStaff(Page<Staff> page) {
		try {
			page.getParams().put("departmentIds", transferList2QueryStr(this.departmentService.findMyDepartmentAndChildrenDeptIds()));
			return page(this.staffService.listStaff(page));
		} catch (Exception e) {
			logger.error("查询用户信息出错", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 将部门id集合转为带","号的字符串
	 * @param deptIds
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午9:01:39
	 */
	private String transferList2QueryStr(List<String> deptIds) {
		StringBuilder sb = new StringBuilder();
		for (String id : deptIds) {
			if (!sb.toString().isEmpty()) {
				sb.append(",");
			}
			sb.append(id);
		}
		return sb.toString();
	}

	/**
	 * 根据员工信息展示对应部门-城市-角色信息
	 * @param staff
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 上午9:01:44
	 */
	private Map<String, Object> packStaffOthers(Staff staff) throws Exception {
		Map<String, Object> others = new HashMap<String, Object>();
		if (isNotEmpty(staff.getDepartmentId())) {
			List<Department> list = this.departmentService.listPathFromRootToCurrentDepartmentId(staff.getDepartmentId());
			if ((null != list) && (list.size() > 0)) {
				StringBuilder sb = new StringBuilder();
				for (int i = list.size() - 1; i >= 0; i--)
					if (((Department) list.get(i)).getDepartmentId().longValue() != -999L) {
						if (!sb.toString().isEmpty()) {
							sb.append("/");
						}
						sb.append(((Department) list.get(i)).getDepartmentName());
					}
				others.put("departmentName", sb.toString());
			}
		}
		if (null != staff.getCityId()) {
			City city = this.cityService.get(staff.getCityId());
			if (null != city) {
				others.put("city", city);
			}
		}
		List<Role> roles = this.roleService.listStaffRoles(staff.getStaffId());
		others.put("roles", roles);
		return others;
	}

	/**
	 * 锁定用户
	 * @param operation
	 * @param staffId
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午9:05:44
	 */
	@RequestMapping(value = { "/lockStaff.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> lockStaff(
			@RequestParam String operation, @RequestParam Long staffId) {
		try {
			if ((isEmpty(operation)) || (isEmpty(staffId))) {
				throw new Exception("参数为空");
			}
			if ("lock".equalsIgnoreCase(operation)) {
				this.staffService.lockStaff(staffId);
				return success("锁定用户成功。");
			}
			this.staffService.unlockStaff(staffId);
			return success("解锁用户成功。");
		} catch (Exception e) {
			logger.error("锁定/解锁用户出错", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 更新用户角色
	 * @param operation
	 * @param staffId
	 * @param roleId
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午9:05:54
	 */
	@RequestMapping(value = { "/updateStaffRole.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateStaffRole(
			@RequestParam String operation, @RequestParam Long staffId,
			@RequestParam Long roleId) {
		try {
			if ((isEmpty(operation)) || (isEmpty(staffId)) || (isEmpty(roleId))) {
				throw new Exception("参数为空");
			}
			if ("add".equalsIgnoreCase(operation)) {
				this.staffService.insertStaffRole(staffId, roleId);
				return success("添加角色成功。");
			}
			Role role = this.roleService.findRole(roleId);
			Staff staff = StaffUtil.getLoginStaff();
			if ((staffId.equals(staff.getStaffId())) && ("1001".equals(role.getRoleKey()))) {
				throw new Exception("超级管理员不能删除自己拥有的系统管理角色");
			}
			this.staffService.deleteStaffRole(staffId, roleId);
			return success("删除角色成功。");
		} catch (Exception e) {
			logger.error("删除角色出错", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 用户角色列表
	 * @param staffId
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午9:06:13
	 */
	@RequestMapping({ "/listStaffRoles.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> listStaffRoles(Long staffId) {
		try {
			if (staffId == null) {
				throw new Exception("用户id为空");
			}
			return page(this.staffService.listStaffRoles(staffId));
		} catch (Exception e) {
			logger.error("错误:", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 检查登录名是否存在
	 * @param loginName
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午9:06:39
	 */
	@RequestMapping({ "/checkStaffLoginName.ajax" })
	@ResponseBody
	public String checkStaffLoginName(String loginName) {
		try {
			if (isEmpty(loginName)) {
				return "参数为空";
			}
			Staff staff = this.staffService.findStaffByLoginName(loginName);
			if (staff != null)
				return "用户名已存在";
		} catch (Exception e) {
			logger.error(new StringBuilder().append("检查用户名[").append(loginName).append("]是否存在出错").toString(), e);
			return "发生系统异常";
		}
		return "true";
	}

	/**
	 * 检查手机号码是否存在
	 * @param mobile
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午9:06:57
	 */
	@RequestMapping({ "/checkStaffMobile.ajax" })
	@ResponseBody
	public String checkStaffMobile(String mobile) {
		try {
			if (isEmpty(mobile)) {
				return "参数为空";
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mobile", mobile);
			Staff staff = this.staffService.findStaffByMap(params);
			if (staff != null)
				return "手机号码已存在";
		} catch (Exception e) {
			logger.error(new StringBuilder().append("检查手机号码[").append(mobile).append("]是否存在出错").toString(), e);
			return "发生系统异常";
		}
		return "true";
	}

	/**
	 * 检查用户的邮箱是否存在
	 * @param email
	 * @return
	 * @author WML
	 * 2016年11月8日 - 上午9:07:12
	 */
	@RequestMapping({ "/checkStaffEmail.ajax" })
	@ResponseBody
	public String checkStaffEmail(String email) {
		try {
			if (isEmpty(email)) {
				return "参数为空";
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("email", email);
			Staff staff = this.staffService.findStaffByMap(params);
			if (staff != null)
				return "邮箱已存在";
		} catch (Exception e) {
			logger.error(new StringBuilder().append("检查邮箱[").append(email).append("]是否存在出错").toString(), e);
			return "发生系统异常";
		}
		return "true";
	}

	/**
	 * 查询登录用户信息
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 上午9:07:25
	 */
	@RequestMapping({ "/findLoginStaff.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> findLoginStaff() throws Exception {
		try {
			Staff staff = StaffUtil.getLoginStaff();
			if (staff == null) {
				throw new Exception("用户没有登录");
			}
			Staff staff2 = this.staffService.findStaffByLoginName(staff.getLoginName());
			staff2.setOthers(packStaffOthers(staff2));
			return success(staff2);
		} catch (Exception e) {
			logger.error("获取登录用户信息出错=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 修改密码
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 上午9:07:42
	 */
	@RequestMapping({ "/changePwd.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> changePwd(String oldPassword, String newPassword) throws Exception {
		try {
			Staff staff = StaffUtil.getLoginStaff();
			if (staff == null) {
				throw new Exception("用户没有登录");
			}
			if ((isEmpty(oldPassword)) || (isEmpty(newPassword))) {
				throw new IllegalArgumentException("旧密码或新密码没有设置！");
			}
			String oldPasswordDecrypt = RSAUtil.decryptString(oldPassword);
			String newPasswordDecrypt = RSAUtil.decryptString(newPassword);
			this.staffService.changePassword(staff.getLoginName(), oldPasswordDecrypt, newPasswordDecrypt);

			return success("修改成功");
		} catch (Exception e) {
			logger.error("获取登录用户信息出错=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 重置密码
	 * @param loginName
	 * @param password
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 上午9:08:01
	 */
	@RequestMapping({ "/resetPwd.ajax" })
	@ResponseBody
	public Map<String, ? extends Object> resetPwd(String loginName,
			String password) throws Exception {
		try {
			if (isEmpty(password)) {
				throw new IllegalArgumentException("新密码没有设置！");
			}
			String passwordDecrypt = RSAUtil.decryptString(password);
			this.staffService.resetPassword(loginName, passwordDecrypt);
			return success("重置成功");
		} catch (Exception e) {
			logger.error("获取登录用户信息出错=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 更新用户信息
	 * @param staff
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 上午9:08:11
	 */
	@RequestMapping(value = { "/updateStaff.ajax" }, method = {RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateStaff(Staff staff)
			throws Exception {
		try {
			if (isEmpty(staff.getLoginName())) {
				throw new Exception("参数错误");
			}
			Staff staff2 = this.staffService.findStaffByLoginName(staff.getLoginName());
			staff2.setRealName(staff.getRealName());
			staff2.setSex(staff.getSex());
			staff2.setMobile(staff.getMobile());
			staff2.setTelephone(staff.getTelephone());
			staff2.setEmail(staff.getEmail());
			staff2.setExtendProperties(staff.getExtendProperties());
			this.staffService.updateStaff(staff2);
			StaffUtil.updateLoginStaff(staff2);
			return success("修改成功");
		} catch (Exception e) {
			logger.error("获取登录用户信息出错=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 添加组织信息
	 * @param departmentId
	 * @param staffIds
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 上午9:08:39
	 */
	@RequestMapping(value = { "/updateStaffDepartment.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateStaffDepartment(
			Long departmentId, String staffIds) throws Exception {
		try {
			if (isEmpty(departmentId)) {
				throw new Exception("参数错误");
			}
			this.staffService.updateStaffDepartment(departmentId, staffIds);
			return success("添加成功");
		} catch (Exception e) {
			logger.error("添加用户到组织出错=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 根据部门和用户id查询角色列表
	 * @param departmentId
	 * @param staffIds
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 上午9:08:56
	 */
	@RequestMapping(value = { "/listRoleByStaffIds.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> listRoleByStaffIds(Long departmentId, String staffIds) throws Exception {
		try {
			return success(this.staffService.listRoleByStaffIds(departmentId, staffIds));
		} catch (Exception e) {
			logger.error("添加用户到组织出错=>", e);
			return fail(e.getMessage());
		}
	}

	/**
	 * 更新用户角色信息
	 * @param departmentId
	 * @param staffIds
	 * @param staffIdRoles
	 * @return
	 * @throws Exception
	 * @author WML
	 * 2016年11月8日 - 上午9:09:47
	 */
	@RequestMapping(value = { "/updateStaffRolesDepartment.ajax" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, ? extends Object> updateStaffRolesDepartment(Long departmentId, String staffIds, String staffIdRoles)
			throws Exception {
		try {
			if ((isEmpty(departmentId)) || (isEmpty(staffIds))) {
				throw new Exception("参数错误");
			}
			this.staffService.updateStaffRolesDepartment(departmentId, staffIds, staffIdRoles);
			return success("修改成功");
		} catch (Exception e) {
			logger.error("添加用户到组织出错=>", e);
			return fail(e.getMessage());
		}
	}
}