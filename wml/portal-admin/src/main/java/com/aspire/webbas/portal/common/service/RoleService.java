package com.aspire.webbas.portal.common.service;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.RoleResourceOperation;
import com.aspire.webbas.portal.common.tree.TreeNode;
import java.util.List;

public abstract interface RoleService {
	public abstract void createRole(Role paramRole) throws Exception;

	public abstract void updateRole(Role paramRole) throws Exception;

	public abstract void deleteRole(Long paramLong) throws Exception;

	public abstract Role findRole(Long paramLong) throws Exception;

	public abstract Page<Role> listRole(Page<Role> paramPage) throws Exception;

	public abstract void updateRoleResource(Long paramLong,
			List<RoleResourceOperation> paramList) throws Exception;

	public abstract List<Role> listStaffRoles(Long paramLong) throws Exception;

	public abstract List<Role> listDepartmentRoles(Long paramLong)
			throws Exception;

	public abstract List<TreeNode> buildRoleResourceTree();

	public abstract List<TreeNode> buildRoleResourceTree(Long paramLong);

	public abstract Role findRoleByKey(String paramString);
}