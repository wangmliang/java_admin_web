package com.aspire.webbas.portal.common.service.impl;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.core.util.StringTools;
import com.aspire.webbas.portal.common.dao.DepartmentDao;
import com.aspire.webbas.portal.common.dao.RoleDao;
import com.aspire.webbas.portal.common.dao.StaffDao;
import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.SecStaffDepartmentRoleKey;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.entity.StaffExtendProperty;
import com.aspire.webbas.portal.common.service.StaffService;
import com.aspire.webbas.portal.common.util.PasswordAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("staffService")
public class StaffServiceImpl implements StaffService {

	@Autowired
	private StaffDao staffDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private DepartmentDao departmentDao;

	public boolean isStaffEmailExsit(String email) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("email", email);
		return findStaffByMap(queryParams) != null;
	}

	public boolean isStaffMobileExsit(String mobile) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("mobile", mobile);
		return findStaffByMap(queryParams) != null;
	}

	@Transactional(rollbackFor = { Exception.class })
	public void createStaff(Staff staff) throws Exception {
		_createStaff(staff);
	}

	private void _createStaff(Staff staff) throws Exception {
		if (this.staffDao.findStaffByLoginName(staff.getLoginName()) != null) {
			throw new Exception("此帐号已注册，系统不允许重复注册!");
		}

		this.staffDao.insertStaff(staff);

		PasswordAdapter pa = new PasswordAdapter(staff);

		staff.setPassword(pa.encryptPassword());

		this.staffDao.updateStaffPassword(staff);
		String roles = (String) staff.getOthers().get("roles");
		if (StringTools.isNotEmptyString(roles)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("departmentId", staff.getDepartmentId());
			String[] roleList = roles.split(",");
			for (int i = 0; i < roleList.length; i++) {
				if (StringTools.isNotEmptyString(roleList[i])) {
					params.put("staffId", staff.getStaffId());
					params.put("roleId", roleList[i]);
					this.staffDao.insertStaffRoles(params);
				}
			}
		}
		for (Entry<String, String> entry : staff.getExtendProperties()
				.entrySet()) {
			// for (Map.Entry entry : staff.getExtendProperties().entrySet()) {
			StaffExtendProperty property = new StaffExtendProperty();
			property.setStaffId(staff.getStaffId());
			property.setProperty((String) entry.getKey());
			property.setValue((String) entry.getValue());

			this.staffDao.insertStaffExtendProperties(property);
		}
	}

	@Transactional(rollbackFor = { Exception.class })
	public void createStaff(Staff staff, String roleIds) throws Exception {
		if (staff.getDepartmentId() == null) {
			throw new IllegalArgumentException("组织ID不能为空。");
		}
		if (this.departmentDao.findDepartment(staff.getDepartmentId()) == null) {
			throw new IllegalArgumentException("通过departmentId="
					+ staff.getDepartmentId() + "，查询不到组织。");
		}
		_createStaff(staff);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("departmentId", staff.getDepartmentId());
		String[] roleIdArray = roleIds.split(",");
		for (String roleId : roleIdArray)
			if (!StringUtils.isEmpty(roleId)) {
				if (this.roleDao.findRole(Long.valueOf(roleId)) == null) {
					throw new IllegalArgumentException("通过roleId=" + roleId
							+ "，查询不到角色信息。");
				}
				params.put("staffId", staff.getStaffId());
				params.put("roleId", roleId);
				this.staffDao.insertStaffRoles(params);
			}
	}

	@Transactional(rollbackFor = { Exception.class })
	public void updateStaff(Staff staff) throws Exception {
		Staff oldStaff = this.staffDao.findStaff(staff.getStaffId());
		if (null == oldStaff) {
			throw new Exception("修改的用户不存在!");
		}

		this.staffDao.updateStaff(staff);
		List<StaffExtendProperty> staffExtendProperties = this.staffDao
				.listStaffExtendProperties(staff.getStaffId());

		for (Entry<String, String> entry : staff.getExtendProperties()
				.entrySet()) {
			// for (Map.Entry entry : staff.getExtendProperties().entrySet()) {
			StaffExtendProperty property = new StaffExtendProperty();
			property.setStaffId(staff.getStaffId());
			property.setProperty((String) entry.getKey());
			property.setValue((String) entry.getValue());

			if (isStaffExtendPropertyExsitProperty(staffExtendProperties,
					property.getProperty()))
				this.staffDao.updateStaffExtendProperties(property);
			else
				this.staffDao.insertStaffExtendProperties(property);
		}
	}

	private boolean isStaffExtendPropertyExsitProperty(
			List<StaffExtendProperty> staffExtendProperties, String property) {
		if (StringTools.isEmptyString(property)) {
			return false;
		}
		for (StaffExtendProperty staffExtendProperty : staffExtendProperties) {
			if (property.equals(staffExtendProperty.getProperty())) {
				return true;
			}
		}
		return false;
	}

	@Transactional(rollbackFor = { Exception.class })
	public void deleteStaffs(Long[] staffIds) throws Exception {
		if (staffIds == null) {
			throw new Exception("参数非法，不能为空");
		}
		for (Long staffId : staffIds) {
			this.staffDao.deleteStaffRoles(staffId);

			this.staffDao.deleteStaff(staffId);
		}
	}

	@Transactional(rollbackFor = { Exception.class })
	public Staff findStaff(Long staffId) {
		Staff staff = this.staffDao.findStaff(staffId);

		if (staff != null) {
			List<StaffExtendProperty> properties = this.staffDao
					.listStaffExtendProperties(staffId);

			for (StaffExtendProperty p : properties) {
				staff.addExtendProperty(p.getProperty(), p.getValue());
			}

			if (staff.getDepartmentId() != null) {
				staff.setDepartment(this.departmentDao.findDepartment(staff
						.getDepartmentId()));
			}
		}

		return staff;
	}

	@Transactional(rollbackFor = { Exception.class })
	public Staff findStaffByLoginName(String loginName) throws Exception {
		if (loginName == null) {
			throw new Exception("登录名[" + loginName + "]不能为空.");
		}

		Staff staff = this.staffDao.findStaffByLoginName(loginName);

		if (staff != null) {
			List<StaffExtendProperty> properties = this.staffDao
					.listStaffExtendProperties(staff.getStaffId());

			for (StaffExtendProperty p : properties) {
				staff.addExtendProperty(p.getProperty(), p.getValue());
			}

			if (staff.getDepartmentId() != null) {
				staff.setDepartment(this.departmentDao.findDepartment(staff
						.getDepartmentId()));
			}
		}

		return staff;
	}

	@Transactional(rollbackFor = { Exception.class })
	public Page<Staff> listStaff(Page<Staff> page) throws Exception {
		if (page == null) {
			throw new Exception("查询参数不能为空");
		}

		page.setDatas(this.staffDao.listStaff(page));

		return page;
	}

	@Transactional(rollbackFor = { Exception.class })
	public void changePassword(String loginName, String oldPassword,
			String newPassword) throws Exception {
		if ((loginName == null) || (oldPassword == null)
				|| (newPassword == null)) {
			throw new IllegalArgumentException("用户名、旧密码或新密码没有设置！");
		}

		if (oldPassword.equals(newPassword)) {
			throw new Exception("旧密码和新密码不能相同！");
		}

		Staff staff = this.staffDao.findStaffByLoginName(loginName);
		if (staff == null) {
			throw new Exception("找不到对应的成员【loginName=" + loginName + "】");
		}

		if (!staff.getPassword().equals(buildStaffPassword(staff, oldPassword))) {
			throw new Exception("旧密码不正确！");
		}

		staff.setPassword(buildStaffPassword(staff, newPassword));

		if ((staff.getStatus() == Staff.Status.INITIAL)
				|| (staff.getStatus() == Staff.Status.PASSWORD_EXPIRED)) {
			staff.setStatus(Staff.Status.NORMAL);
		}

		this.staffDao.updateStaffPassword(staff);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void resetPassword(String loginName, String newPassword)
			throws Exception {
		if ((loginName == null) || (newPassword == null)) {
			throw new IllegalArgumentException("用户名或新密码没有设置！");
		}

		Staff staff = this.staffDao.findStaffByLoginName(loginName);
		if (staff == null) {
			throw new Exception("找不到对应的成员【loginName=" + loginName + "】");
		}

		staff.setPassword(buildStaffPassword(staff, newPassword));

		staff.setStatus(Staff.Status.INITIAL);

		this.staffDao.updateStaffPassword(staff);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void lockStaff(Long staffId) throws Exception {
		Staff staff = this.staffDao.findStaff(staffId);
		if (staff == null) {
			throw new Exception("用户[" + staffId + "]没有找到");
		}

		if ((staff.getStatus() == Staff.Status.INITIAL)
				|| (staff.getStatus() == Staff.Status.NORMAL)) {
			staff.setStatus(Staff.Status.LOCKED);

			this.staffDao.updateStaff(staff);
		} else {
			throw new Exception("用户状态不是初创或者正常状态,不允许锁定.");
		}
	}

	@Transactional(rollbackFor = { Exception.class })
	public void unlockStaff(Long staffId) throws Exception {
		Staff staff = this.staffDao.findStaff(staffId);
		if (staff == null) {
			throw new Exception("用户[" + staffId + "]没有找到");
		}

		staff.setStatus(Staff.Status.NORMAL);

		this.staffDao.updateStaff(staff);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void updateStaffRoles(Long staffId, List<Long> roleIds)
			throws Exception {
		Staff staff = this.staffDao.findStaff(staffId);
		if (staff == null) {
			throw new Exception("用户[" + staffId + "]没有找到");
		}

		if (staff.getDepartmentId() == null) {
			staff.setDepartmentId(new Long(-1L));
		}

		this.staffDao.deleteStaffRoles(staff.getStaffId());

		for (Long roleId : roleIds) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("staffId", staff.getStaffId());
			map.put("departmentId", staff.getDepartmentId());
			map.put("roleId", roleId);

			this.staffDao.insertStaffRoles(map);
		}
	}

	@Transactional(rollbackFor = { Exception.class })
	public void insertStaffRole(Long staffId, Long roleId) throws Exception {
		Staff staff = this.staffDao.findStaff(staffId);
		if (staff == null) {
			throw new Exception("用户[" + staffId + "]没有找到");
		}

		if (staff.getDepartmentId() == null) {
			staff.setDepartmentId(new Long(-1L));
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staffId", staff.getStaffId());
		map.put("departmentId", staff.getDepartmentId());
		map.put("roleId", roleId);

		this.staffDao.insertStaffRoles(map);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void deleteStaffRole(Long staffId, Long roleId) throws Exception {
		Staff staff = this.staffDao.findStaff(staffId);
		if (staff == null) {
			throw new Exception("用户[" + staffId + "]没有找到");
		}

		if (staff.getDepartmentId() == null) {
			staff.setDepartmentId(new Long(-1L));
		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("staffId", staff.getStaffId());
		param.put("roleId", roleId);

		this.staffDao.deleteStaffRolesByStaffIdAndRoleId(param);
	}

	public List<Staff> listDepartmentStaffs(Long departmentId) throws Exception {
		Page<Staff> page = new Page<Staff>();

		page.addParam("departmentId", departmentId.toString());
		page.addParam("noAdmin", "1");

		page.setRows(Integer.valueOf(999999));

		return this.staffDao.listStaff(page);
	}

	public List<Staff> listDepartmentAllStaffs(Long departmentId,
			String keyword, String domain) throws Exception {
		if (departmentId == null) {
			throw new Exception("departmentId不能为空");
		}
		Page<Staff> page = new Page<Staff>();

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("departmentId", departmentId);

		if (domain != null) {
			param.put("domain", domain);
		}

		List<Department> departs = this.departmentDao
				.listSelfAndSubDepartmentByDepartmentId(param);

		StringBuffer ids = new StringBuffer();
		for (Department d : departs) {
			ids.append(d.getDepartmentId().toString()).append(",");
		}

		if (ids.length() > 0)
			ids.delete(ids.length() - 1, ids.length());
		else {
			return new ArrayList<Staff>();
		}

		page.addParam("departmentIds", ids.toString());

		if ((keyword != null) && (!"".equals(keyword.trim()))) {
			page.addParam("keyword", keyword);
		}
		page.setRows(Integer.valueOf(999999));

		return this.staffDao.listStaff(page);
	}

	public List<Staff> listStaffsByDomain(String domain, String keyword)
			throws Exception {
		if (domain == null) {
			throw new Exception("domain不能为空");
		}
		Department dept = new Department();
		dept.setDomain(domain);

		List<Department> departs = this.departmentDao.listDepartment(dept);

		StringBuffer ids = new StringBuffer();
		for (Department d : departs) {
			ids.append(d.getDepartmentId().toString()).append(",");
		}

		if (ids.length() > 0)
			ids.delete(ids.length() - 1, ids.length());
		else {
			return new ArrayList<Staff>();
		}

		Page<Staff> page = new Page<Staff>();
		page.addParam("departmentIds", ids.toString());

		if ((keyword != null) && (!"".equals(keyword.trim()))) {
			page.addParam("keyword", keyword);
		}

		page.setRows(Integer.valueOf(999999));

		return this.staffDao.listStaff(page);
	}

	@Transactional(rollbackFor = { Exception.class })
	public List<Staff> listStaffByRole(Long roleId) throws Exception {
		return this.staffDao.listStaffByRole(roleId);
	}

	private String buildStaffPassword(Staff staff, String password) {
		PasswordAdapter pa = new PasswordAdapter(staff);

		staff.setPassword(password);

		return pa.encryptPassword();
	}

	public Page<Role> listStaffRoles(Long staffId) throws Exception {
		Page<Role> page = new Page<Role>();
		page.setRows(Integer.valueOf(99999));
		Staff staff = this.staffDao.findStaff(staffId);
		if ((null != staff) && (staff.getDepartmentId() != null)) {
			page.addParam("departmentId", staff.getDepartmentId().toString());
		}
		List<Role> allRoles = this.roleDao.listRole(page);

		Map<Long, Role> roleMap = createRoleMap(this.roleDao
				.listStaffRoles(new Long(staffId.longValue())));

		for (Role r : allRoles) {
			if (roleMap.containsKey(r.getRoleId()))
				r.addOtherField("check", "true");
			else {
				r.addOtherField("check", "false");
			}
		}
		page.setDatas(allRoles);
		return page;
	}

	private Map<Long, Role> createRoleMap(List<Role> list) {
		Map<Long, Role> roleMap = new HashMap<Long, Role>();

		for (Role r : list) {
			roleMap.put(r.getRoleId(), r);
		}

		return roleMap;
	}

	@Transactional(rollbackFor = { Exception.class })
	public void updateStaffDepartment(Long departmentId, String staffIds)
			throws Exception {
		this.staffDao.clearStaffDepartment(departmentId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("departmentId", departmentId);
		if (!StringUtils.isEmpty(staffIds)) {
			String[] arr = staffIds.split(",");
			params.put("staffIds", arr);
			this.staffDao.updateStaffDepartmentByStaffIds(params);
		} else {
			this.staffDao.clearStaffDepartment(departmentId);
		}
		this.staffDao.deleteStaffRolesByDepartmentIdAndNotInStaffIds(params);
	}

	public List<SecStaffDepartmentRoleKey> listRoleByStaffIds(
			Long departmentId, String staffIds) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("departmentId", departmentId);
		params.put("staffIds", staffIds.split(","));
		return this.staffDao.listRoleByStaffIds(params);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void updateStaffRolesDepartment(Long departmentId, String staffIds,
			String staffIdRoles) throws Exception {
		String[] staffIdArr = staffIds.split(",");
		for (String staffId : staffIdArr) {
			this.staffDao
					.deleteStaffRoles(Long.valueOf(Long.parseLong(staffId)));
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("departmentId", departmentId);
		String[] staffIdRoleArr = staffIdRoles.split(",");
		for (String staffIdRole : staffIdRoleArr)
			if (!StringUtils.isEmpty(staffIdRole)) {
				String[] roles = staffIdRole.split("\\|");
				if (roles.length == 2) {
					params.put("staffId", roles[0]);
					params.put("roleId", roles[1]);
					this.staffDao.insertStaffRoles(params);
				}
			}
	}

	public List<Staff> listStaffs(Long departmentId, String keyword)
			throws Exception {
		Page<Staff> page = new Page<Staff>();
		if (null != departmentId) {
			page.addParam("departmentId", departmentId.toString());
		}
		page.setRows(Integer.valueOf(999999));
		page.addParam("keyword", keyword);

		return this.staffDao.listStaff(page);
	}

	public Staff findStaffByMap(Map<String, Object> params) {
		List<Staff> staffs = this.staffDao.findStaffByMap(params);
		if ((null != staffs) && (!staffs.isEmpty())) {
			return (Staff) staffs.get(0);
		}
		return null;
	}

	public List<StaffExtendProperty> listStaffExtendProperties(Long staffId) {
		return this.staffDao.listStaffExtendProperties(staffId);
	}

	public void insertStaffExtendProperty(StaffExtendProperty data) {
		this.staffDao.insertStaffExtendProperties(data);
	}
}