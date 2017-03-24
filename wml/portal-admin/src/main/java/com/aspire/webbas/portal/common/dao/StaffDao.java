package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.portal.common.entity.SecStaffDepartmentRoleKey;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.entity.StaffExtendProperty;
import java.util.List;
import java.util.Map;

/**
 * 用户Mapper
 * <pre>
 * <b>Title：</b>StaffDao.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月9日 - 上午8:55:56<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface StaffDao {
	/**
	 * 添加
	 * @param paramStaff
	 * @author WML
	 * 2016年11月9日 - 上午8:56:06
	 */
	public abstract void insertStaff(Staff paramStaff);

	/**
	 * 更新
	 * @param paramStaff
	 * @author WML
	 * 2016年11月9日 - 上午8:56:34
	 */
	public abstract void updateStaff(Staff paramStaff);

	/**
	 * 更新密码
	 * @param paramStaff
	 * @author WML
	 * 2016年11月9日 - 上午8:56:41
	 */
	public abstract void updateStaffPassword(Staff paramStaff);

	/**
	 * 删除状态为“DELETED”
	 * @param paramLong	STAFF_ID
	 * @author WML
	 * 2016年11月9日 - 上午8:57:02
	 */
	public abstract void deleteStaff(Long paramLong);

	/**
	 * 删除
	 * @param paramLong	STAFF_ID
	 * @author WML
	 * 2016年11月9日 - 上午8:57:07
	 */
	public abstract void removeStaff(Long paramLong);

	/**
	 * 根据id查询
	 * @param paramLong	STAFF_ID
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:58:07
	 */
	public abstract Staff findStaff(Long paramLong);

	/**
	 * 多条件查询
	 * @param paramMap
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:58:46
	 */
	public abstract List<Staff> findStaffByMap(Map<String, Object> paramMap);

	/**
	 * 根据loginName查询
	 * @param paramString
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:58:56
	 */
	public abstract Staff findStaffByLoginName(String paramString);

	/**
	 * 根据loginName和password 查询
	 * @param paramStaff
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:59:08
	 */
	public abstract Staff findStaffByNameAndPassword(Staff paramStaff);

	/**
	 * 添加StaffExtendProperties
	 * @param paramStaffExtendProperty
	 * @author WML
	 * 2016年11月9日 - 上午8:59:37
	 */
	public abstract void insertStaffExtendProperties(StaffExtendProperty paramStaffExtendProperty);

	/**
	 * 删除StaffExtendProperties
	 * @param paramLong
	 * @author WML
	 * 2016年11月9日 - 上午9:00:22
	 */
	public abstract void deleteStaffExtendProperties(Long paramLong);

	/**
	 * StaffExtendProperties列表
	 * @param paramLong
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午9:00:57
	 */
	public abstract List<StaffExtendProperty> listStaffExtendProperties(Long paramLong);

	/**
	 * 用户列表
	 * @param paramPage
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午9:01:09
	 */
	public abstract List<Staff> listStaff(Page<Staff> paramPage);

	/**
	 * 添加StaffRoles
	 * @param paramMap
	 * @author WML
	 * 2016年11月9日 - 上午9:01:16
	 */
	public abstract void insertStaffRoles(Map<String, Object> paramMap);

	/**
	 * 删除
	 * @param paramLong
	 * @author WML
	 * 2016年11月9日 - 上午9:03:59
	 */
	public abstract void deleteStaffRoles(Long paramLong);

	/**
	 * 根据roleId删除
	 * @param paramLong
	 * @author WML
	 * 2016年11月9日 - 上午9:04:15
	 */
	public abstract void deleteStaffRolesByRoleId(Long paramLong);

	/**
	 * 根据staffID和roleID删除
	 * @param paramMap
	 * @author WML
	 * 2016年11月9日 - 上午9:04:31
	 */
	public abstract void deleteStaffRolesByStaffIdAndRoleId(Map<String, Object> paramMap);

	/**
	 * 根据DepartmentIdAndRoleId删除
	 * @param paramMap
	 * @author WML
	 * 2016年11月9日 - 上午9:04:53
	 */
	public abstract void deleteStaffRolesByDepartmentIdAndRoleId(Map<String, Object> paramMap);

	/**
	 * 根据DepartmentIdAndNotInStaffIds删除
	 * @param paramMap
	 * @author WML
	 * 2016年11月9日 - 上午9:05:06
	 */
	public abstract void deleteStaffRolesByDepartmentIdAndNotInStaffIds(Map<String, Object> paramMap);

	/**
	 * 添加
	 * @param paramMap
	 * @author WML
	 * 2016年11月9日 - 上午9:05:27
	 */
	public abstract void insertStaffRolesByKey(Map<String, String> paramMap);

	/**
	 * 列表
	 * @param paramLong
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午9:05:33
	 */
	public abstract List<Staff> listStaffByRole(Long paramLong);

	/**
	 * 清除StaffDepartment
	 * @param paramLong
	 * @author WML
	 * 2016年11月9日 - 上午9:05:38
	 */
	public abstract void clearStaffDepartment(Long paramLong);

	/**
	 * 更新
	 * @param paramMap
	 * @author WML
	 * 2016年11月9日 - 上午9:06:25
	 */
	public abstract void updateStaffDepartment(Map<String, String> paramMap);

	/**
	 * 根据DEPARTMENT_ID和StaffIds更新
	 * @param paramMap
	 * @author WML
	 * 2016年11月9日 - 上午9:06:35
	 */
	public abstract void updateStaffDepartmentByStaffIds(Map<String, Object> paramMap);

	/**
	 * 根据DEPARTMENT_ID和staffIds查询
	 * @param paramMap
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午9:07:23
	 */
	public abstract List<SecStaffDepartmentRoleKey> listRoleByStaffIds(Map<String, Object> paramMap);

	/**
	 * 更新
	 * @param paramStaffExtendProperty
	 * @author WML
	 * 2016年11月9日 - 上午9:07:16
	 */
	public abstract void updateStaffExtendProperties(StaffExtendProperty paramStaffExtendProperty);
}