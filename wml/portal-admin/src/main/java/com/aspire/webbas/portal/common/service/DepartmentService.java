package com.aspire.webbas.portal.common.service;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.portal.common.entity.Department;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.tree.TreeNode;
import java.util.List;

public abstract interface DepartmentService {
	public abstract void insertDepartment(Department paramDepartment)
			throws Exception;

	public abstract void updateDepartment(Department paramDepartment)
			throws Exception;

	public abstract void deleteDepartment(Long paramLong) throws Exception;

	public abstract Department findDepartment(Long paramLong) throws Exception;

	public abstract List<Department> listDepartment(Department paramDepartment)
			throws Exception;

	public abstract Page<Role> listDepartmentRoles(Long paramLong)
			throws Exception;

	public abstract void deleteDepartmentRoleByRoleIdAndDeptId(Long paramLong1,
			Long paramLong2) throws Exception;

	public abstract void insertDepartmentRole(Long paramLong1, Long paramLong2)
			throws Exception;

	public abstract List<TreeNode> buildDepartmentTree() throws Exception;

	public abstract List<String> findMyDepartmentAndChildrenDeptIds()
			throws Exception;

	public abstract List<Department> listPathFromRootToCurrentDepartmentId(
			Long paramLong) throws Exception;
}