package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.portal.common.entity.Department;
import java.util.List;
import java.util.Map;

/**
 * 部门Mapper
 * <pre>
 * <b>Title：</b>DepartmentDao.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:30:36<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface DepartmentDao {
	/**
	 * 添加（过滤空属性）
	 * @param paramDepartment
	 * @author WML
	 * 2016年11月8日 - 下午5:30:44
	 */
	public abstract void insertDepartment(Department paramDepartment);

	/**
	 * 更新
	 * @param paramDepartment
	 * @author WML
	 * 2016年11月8日 - 下午5:31:00
	 */
	public abstract void updateDepartment(Department paramDepartment);

	/**
	 * 根据id删除
	 * @param paramLong
	 * @author WML
	 * 2016年11月8日 - 下午5:31:13
	 */
	public abstract void deleteDepartment(Long paramLong);

	/**
	 * 根据id查询
	 * @param paramLong
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:31:23
	 */
	public abstract Department findDepartment(Long paramLong);

	/**
	 * 部门列表
	 * @param paramDepartment
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:31:30
	 */
	public abstract List<Department> listDepartment(Department paramDepartment);

	/**
	 * 添加部门信息
	 * @param paramMap
	 * @author WML
	 * 2016年11月8日 - 下午5:31:41
	 */
	public abstract void insertDepartmentRoles(Map<String, Object> paramMap);

	/**
	 * 删除部门
	 * @param paramLong
	 * @author WML
	 * 2016年11月8日 - 下午5:31:47
	 */
	public abstract void deleteDepartmentRoles(Long paramLong);

	/**
	 * 根据角色id删除部门角色关联表
	 * @param paramLong
	 * @author WML
	 * 2016年11月8日 - 下午5:31:53
	 */
	public abstract void deleteDepartmentRolesByRoleId(Long paramLong);

	/**
	 * 根据部门id和角色id删除部门及角色关联表
	 * @param paramMap
	 * @author WML
	 * 2016年11月8日 - 下午5:32:15
	 */
	public abstract void deleteDepartmentRolesByRoleIdAndDeptId(Map<String, Object> paramMap);

	/**
	 * 根据部门id查询部门列表
	 * @param paramLong
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:32:39
	 */
	public abstract List<Department> listPathFromRootToCurrentDepartmentId(Long paramLong);

	/**
	 * 根据部门id查询子部门列表
	 * @param paramMap
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:33:11
	 */
	public abstract List<Department> listSelfAndSubDepartmentByDepartmentId(Map<String, Object> paramMap);
}