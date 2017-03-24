package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.RoleResourceOperation;
import java.util.List;
import java.util.Map;

/**
 * 角色Mapper
 * <pre>
 * <b>Title：</b>RoleDao.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月9日 - 上午8:51:00<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface RoleDao {
	
	/**
	 * 添加
	 * @param paramRole
	 * @author WML
	 * 2016年11月9日 - 上午8:51:19
	 */
	public abstract void insertRole(Role paramRole);

	/**
	 * 根据id删除
	 * @param paramLong
	 * @author WML
	 * 2016年11月9日 - 上午8:51:32
	 */
	public abstract void deleteRole(Long paramLong);

	/**
	 * 更新
	 * @param paramRole
	 * @author WML
	 * 2016年11月9日 - 上午8:51:57
	 */
	public abstract void updateRole(Role paramRole);

	/**
	 * 根据id查询
	 * @param paramLong
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:52:04
	 */
	public abstract Role findRole(Long paramLong);

	/**
	 * 根据key查询
	 * @param paramString
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:52:10
	 */
	public abstract Role findRoleByKey(String paramString);

	/**
	 * 根据name查询
	 * @param paramString
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:52:22
	 */
	public abstract Role findRoleByName(String paramString);

	/**
	 * 角色列表
	 * @param paramPage
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:52:33
	 */
	public abstract List<Role> listRole(Page<Role> paramPage);

	/**
	 * 根据用户id查询
	 * @param paramLong	STAFF_ID
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:52:49
	 */
	public abstract List<Role> listStaffRoles(Long paramLong);

	/**
	 * 根据DEPARTMENT_ID查询
	 * @param paramLong	DEPARTMENT_ID
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:53:23
	 */
	public abstract List<Role> listDepartmentRoles(Long paramLong);

	/**
	 * 添加RoleResourceOperation
	 * @param paramRoleResourceOperation
	 * @author WML
	 * 2016年11月9日 - 上午8:54:50
	 */
	public abstract void insertRoleResourceOperation(RoleResourceOperation paramRoleResourceOperation);

	/**
	 * 删除RoleResourceOperation
	 * @param paramMap
	 * @author WML
	 * 2016年11月9日 - 上午8:55:00
	 */
	public abstract void deleteRoleResourceOperation(Map<String, Object> paramMap);

	/**
	 * 根据id删除RoleResourceOperation
	 * @param paramLong
	 * @author WML
	 * 2016年11月9日 - 上午8:54:57
	 */
	public abstract void deleteRoleResourceOperationById(Long paramLong);
}