package com.aspire.webbas.portal.common.service.impl;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.portal.common.dao.DepartmentDao;
import com.aspire.webbas.portal.common.dao.OperationDao;
import com.aspire.webbas.portal.common.dao.ResourceCategoryDao;
import com.aspire.webbas.portal.common.dao.ResourceDao;
import com.aspire.webbas.portal.common.dao.RoleDao;
import com.aspire.webbas.portal.common.dao.StaffDao;
import com.aspire.webbas.portal.common.entity.Operation;
import com.aspire.webbas.portal.common.entity.Resource;
import com.aspire.webbas.portal.common.entity.ResourceCategory;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.RoleResourceOperation;
import com.aspire.webbas.portal.common.service.RoleService;
import com.aspire.webbas.portal.common.tree.CheckTreeNode;
import com.aspire.webbas.portal.common.tree.TreeBuilder;
import com.aspire.webbas.portal.common.tree.TreeNode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private StaffDao staffDao;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private OperationDao operationDao;

	@Autowired
	private ResourceDao resourceDao;

	@Autowired
	private ResourceCategoryDao resourceCategoryDao;

	@Transactional(rollbackFor = { Exception.class })
	public void createRole(Role role) throws Exception {
		if (null != this.roleDao.findRoleByKey(role.getRoleKey())) {
			throw new Exception("角色助记码不能重复！");
		}
		if (null != this.roleDao.findRoleByName(role.getRoleName())) {
			throw new Exception("角色名称不能重复！");
		}

		this.roleDao.insertRole(role);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void updateRole(Role role) throws Exception {
		Role roleTemp = this.roleDao.findRoleByKey(role.getRoleKey());

		if ((null != roleTemp)
				&& (!role.getRoleId().equals(roleTemp.getRoleId()))) {
			throw new Exception("角色助记码不能重复！");
		}

		roleTemp = this.roleDao.findRoleByName(role.getRoleName());
		if ((null != roleTemp)
				&& (!role.getRoleId().equals(roleTemp.getRoleId()))) {
			throw new Exception("角色名称不能重复！");
		}

		Role oldRole = this.roleDao.findRole(role.getRoleId());

		if (null == oldRole)
			throw new Exception("角色不存在！");
		if (Role.CAN_NOT_MODIFY.equals(oldRole.getCanModify())) {
			throw new Exception("此角色不能修改！");
		}

		role.setCanModify(oldRole.getCanModify());
		role.setAutoAssign(oldRole.getAutoAssign());

		this.roleDao.updateRole(role);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void deleteRole(Long roleId) throws Exception {
		if (roleId == null) {
			throw new Exception("没有指定要删除的角色[roleId=" + roleId + "]");
		}

		Role role = this.roleDao.findRole(roleId);
		if (role == null) {
			throw new Exception("没有指定要删除的角色[roleId=" + roleId + "]");
		}

		this.roleDao.deleteRoleResourceOperationById(roleId);

		this.staffDao.deleteStaffRolesByRoleId(roleId);

		this.departmentDao.deleteDepartmentRolesByRoleId(roleId);

		this.roleDao.deleteRole(roleId);
	}

	@Transactional(rollbackFor = { Exception.class })
	public Role findRole(Long roleId) throws Exception {
		return this.roleDao.findRole(roleId);
	}

	@Transactional(rollbackFor = { Exception.class })
	public Page<Role> listRole(Page<Role> page) throws Exception {
		page.setDatas(this.roleDao.listRole(page));

		return page;
	}

	@Transactional(rollbackFor = { Exception.class })
	public List<Role> listStaffRoles(Long staffId) throws Exception {
		return this.roleDao.listStaffRoles(staffId);
	}

	@Transactional(rollbackFor = { Exception.class })
	public List<Role> listDepartmentRoles(Long departmentId) throws Exception {
		return this.roleDao.listDepartmentRoles(departmentId);
	}

	public List<TreeNode> buildRoleResourceTree() {
		TreeBuilder tree = new TreeBuilder();

		addResourceCategory(tree);

		addResource(tree);

		addOperation(tree);

		return tree.buildTree();
	}

	public List<TreeNode> buildRoleResourceTree(Long roleId) {
		TreeBuilder tree = new TreeBuilder();

		addResourceCategory(tree);

		addResource(tree);

		addOperation(tree);

		List<Operation> oList = this.operationDao.listOperationByRoleId(roleId);

		for (Operation op : oList) {
			String key = op.getResourceId() + "-" + op.getOperationKey();
			tree.markChecked(key);
		}

		return tree.buildTree();
	}

	@Transactional(rollbackFor = { Exception.class })
	public void updateRoleResource(Long roleId, List<RoleResourceOperation> list) {
		if (list != null) {
			this.roleDao.deleteRoleResourceOperationById(roleId);

			for (RoleResourceOperation roleResourceOperation : list) {
				roleResourceOperation.setRoleId(roleId);

				this.roleDao.insertRoleResourceOperation(roleResourceOperation);
			}
		}
	}

	private void addResourceCategory(TreeBuilder tree) {
		ResourceCategory category = new ResourceCategory();

		List<ResourceCategory> rcs = this.resourceCategoryDao
				.listRootResourceCategory(category);

		for (ResourceCategory ca : rcs) {
			TreeNode node = new TreeNode(longToString(ca.getCategoryId()),
					longToString(ca.getParentId()), ca.getCategoryName());

			tree.addNode(node);
		}
	}

	private void addResource(TreeBuilder tree) {
		Resource param = new Resource();

		List<Resource> rList = this.resourceDao.listResource(param);

		for (Resource res : rList) {
			TreeNode node = new TreeNode("res-"
					+ longToString(res.getResourceId()),
					longToString(res.getCategoryId()), res.getResourceName());

			tree.addNode(node);
		}
	}

	private void addOperation(TreeBuilder tree) {
		Operation operParam = new Operation();

		List<Operation> oList = this.operationDao.listOperation(operParam);

		for (Operation op : oList) {
			CheckTreeNode node = new CheckTreeNode(op.getResourceId() + "-"
					+ op.getOperationKey(), "res-" + op.getResourceId(),
					op.getOperationName());

			tree.addNode(node);
		}
	}

	public Role findRoleByKey(String roleKey) {
		return this.roleDao.findRoleByKey(roleKey);
	}

	private String longToString(Long value) {
		if (value == null) {
			return null;
		}

		return value.toString();
	}
}