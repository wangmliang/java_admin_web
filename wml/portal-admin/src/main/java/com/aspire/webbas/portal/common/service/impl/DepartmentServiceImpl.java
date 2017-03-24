package com.aspire.webbas.portal.common.service.impl;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.portal.common.dao.DepartmentDao;
import com.aspire.webbas.portal.common.dao.RoleDao;
import com.aspire.webbas.portal.common.dao.StaffDao;
import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.service.DepartmentService;
import com.aspire.webbas.portal.common.tree.TreeBuilder;
import com.aspire.webbas.portal.common.tree.TreeNode;
import com.aspire.webbas.portal.common.util.StaffUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private StaffDao staffDao;

	public void insertDepartment(Department department) throws Exception {
		if (department == null) {
			throw new Exception("不能插入空部门");
		}
		this.departmentDao.insertDepartment(department);
		Map<String, Object> others = department.getOthers();
		String role = null != others ? (String) others.get("roles") : "";
		if (StringUtils.isNotEmpty(role)) {
			String[] roles = role.split(",");
			for (String r : roles)
				insertDepartmentRole(department.getDepartmentId(),
						Long.valueOf(r));
		}
	}

	@Transactional(rollbackFor = { Exception.class })
	public void updateDepartment(Department department) throws Exception {
		if (department == null) {
			throw new Exception("不能更新空部门");
		}

		Department d = this.departmentDao.findDepartment(department
				.getDepartmentId());

		if (d == null) {
			throw new Exception("找不到对应的部门[id=" + department.getDepartmentId()
					+ "]");
		}

		d.setDepartmentName(department.getDepartmentName());
		d.setDepartmentDesc(department.getDepartmentDesc());
		d.setAddress(department.getAddress());
		d.setEmail(department.getEmail());

		this.departmentDao.updateDepartment(department);

		List<Role> oldDepartmentRoles = this.roleDao
				.listDepartmentRoles(department.getDepartmentId());
		List<Long> roles2Long = new ArrayList<Long>();
		Map<String, Object> others = department.getOthers();
		String role = null != others ? (String) others.get("roles") : "";
		if (StringUtils.isNotEmpty(role)) {
			String[] roles = role.split(",");
			for (String r : roles) {
				roles2Long.add(Long.valueOf(r));
			}
		}
		updateDepartmentRoles(department.getDepartmentId(), roles2Long);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("departmentId", department.getDepartmentId());
		for (Role oldRole : oldDepartmentRoles)
			if (!roles2Long.contains(oldRole.getRoleId())) {
				params.put("roleId", oldRole.getRoleId());
				this.staffDao.deleteStaffRolesByDepartmentIdAndRoleId(params);
			}
	}

	@Transactional(rollbackFor = { Exception.class })
	public void deleteDepartment(Long departmentId) throws Exception {
		Department param = new Department();
		param.setParentId(departmentId);

		List<Department> nextDepts = this.departmentDao.listDepartment(param);

		if (nextDepts.size() > 0) {
			throw new Exception("该部门还存在下级，不允许删除");
		}

		this.departmentDao.deleteDepartmentRoles(departmentId);
		Page<Staff> page = new Page<Staff>();
		page.setRows(Integer.valueOf(99999));
		page.addParam("departmentId", "" + departmentId);
		List<Staff> staffList = this.staffDao.listStaff(page);
		for (Staff staff : staffList) {
			this.staffDao.deleteStaffRoles(staff.getStaffId());
			this.staffDao.removeStaff(staff.getStaffId());
		}

		this.departmentDao.deleteDepartment(departmentId);
	}

	public Department findDepartment(Long departmentId) throws Exception {
		return this.departmentDao.findDepartment(departmentId);
	}

	public List<Department> listDepartment(Department department)
			throws Exception {
		if (department == null) {
			department = new Department();
		}

		return this.departmentDao.listDepartment(department);
	}

	public void updateDepartmentRoles(Long departmentId, List<Long> roleIds)
			throws Exception {
		if ((departmentId == null) || (roleIds == null)) {
			throw new IllegalArgumentException("参数不能为空！");
		}

		this.departmentDao.deleteDepartmentRoles(departmentId);

		for (Long roleId : roleIds) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("departmentId", departmentId);
			map.put("roleId", roleId);

			this.departmentDao.insertDepartmentRoles(map);
		}
	}

	@Transactional(rollbackFor = { Exception.class })
	public void deleteDepartmentRoleByRoleIdAndDeptId(Long departmentId,
			Long roleId) throws Exception {
		if ((departmentId == null) || (roleId == null)) {
			throw new IllegalArgumentException("参数不能为空！");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("departmentId", departmentId);
		map.put("roleId", roleId);

		this.departmentDao.deleteDepartmentRolesByRoleIdAndDeptId(map);
		this.staffDao.deleteStaffRolesByDepartmentIdAndRoleId(map);
	}

	public void insertDepartmentRole(Long departmentId, Long roleId)
			throws Exception {
		if ((departmentId == null) || (roleId == null)) {
			throw new IllegalArgumentException("参数不能为空！");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("departmentId", departmentId);
		map.put("roleId", roleId);

		this.departmentDao.insertDepartmentRoles(map);
	}

	public List<TreeNode> buildDepartmentTree() throws Exception {
		List<Department> depts = this.departmentDao
				.listDepartment(new Department());

		TreeBuilder tree = new TreeBuilder();

		for (Department dept : depts) {
			TreeNode node = new TreeNode(longToString(dept.getDepartmentId()),
					longToString(dept.getParentId()), dept.getDepartmentName());
			tree.addNode(node);
		}
		return filterMyDepartmentTree(tree.buildTree());
	}

	private List<TreeNode> filterMyDepartmentTree(List<TreeNode> treeNodes)
			throws Exception {
		Staff staff = StaffUtil.getLoginStaff();
		if ((null == staff) || (null == staff.getDepartmentId())) {
			return treeNodes;
		}
		String departmentId = longToString(staff.getDepartmentId());
		List<TreeNode> result = new ArrayList<TreeNode>();
		findTreeNodeById(treeNodes, departmentId, result);
		return result;
	}

	private void findTreeNodeById(List<TreeNode> treeNodes,
			String departmentId, List<TreeNode> treeNode) {
		for (TreeNode node : treeNodes) {
			if (node.getId().equals(departmentId)) {
				treeNode.add(node);
				return;
			}
			if ((null != node.getChildren()) && (!node.getChildren().isEmpty()))
				findTreeNodeById(new ArrayList<TreeNode>(node.getChildren()),
						departmentId, treeNode);
		}
	}

	public List<String> findMyDepartmentAndChildrenDeptIds() throws Exception {
		List<TreeNode> treeNodes = filterMyDepartmentTree(buildDepartmentTree());
		List<String> result = new ArrayList<String>();
		transferTreeNodes2DeptIds(treeNodes, result);
		return result;
	}

	private void transferTreeNodes2DeptIds(List<TreeNode> treeNodes,
			List<String> departmentIds) {
		for (TreeNode node : treeNodes) {
			departmentIds.add(node.getId());
			if ((null != node.getChildren()) && (!node.getChildren().isEmpty()))
				transferTreeNodes2DeptIds(
						new ArrayList<TreeNode>(node.getChildren()),
						departmentIds);
		}
	}

	public Page<Role> listDepartmentRoles(Long departmentId) throws Exception {
		Page<Role> page = new Page<Role>();
		page.setRows(Integer.valueOf(99999));
		Staff staff = StaffUtil.getLoginStaff();
		if ((null != staff) && (staff.getDepartmentId() != null)) {
			page.addParam("departmentId", staff.getDepartmentId().toString());
		}
		List<Role> allRoles = this.roleDao.listRole(page);
		Map<Long, Role> roleMap;
		if (null != departmentId) {
			roleMap = createRoleMap(this.roleDao
					.listDepartmentRoles(departmentId));
			for (Role r : allRoles) {
				if (roleMap.containsKey(r.getRoleId()))
					r.addOtherField("check", "true");
				else {
					r.addOtherField("check", "false");
				}
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

	public List<Department> listPathFromRootToCurrentDepartmentId(
			Long departmentId) throws Exception {
		return this.departmentDao
				.listPathFromRootToCurrentDepartmentId(departmentId);
	}

	private String longToString(Long value) {
		if (value == null) {
			return null;
		}

		return value.toString();
	}
}