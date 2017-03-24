package com.aspire.webbas.portal.common.service;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.portal.common.entity.Role;
import com.aspire.webbas.portal.common.entity.SecStaffDepartmentRoleKey;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.entity.StaffExtendProperty;
import java.util.List;
import java.util.Map;

public abstract interface StaffService {
	public abstract void createStaff(Staff paramStaff) throws Exception;

	public abstract void createStaff(Staff paramStaff, String paramString)
			throws Exception;

	public abstract void updateStaff(Staff paramStaff) throws Exception;

	public abstract void changePassword(String paramString1,
			String paramString2, String paramString3) throws Exception;

	public abstract void resetPassword(String paramString1, String paramString2)
			throws Exception;

	public abstract void lockStaff(Long paramLong) throws Exception;

	public abstract void unlockStaff(Long paramLong) throws Exception;

	public abstract void deleteStaffs(Long[] paramArrayOfLong) throws Exception;

	public abstract Staff findStaff(Long paramLong);

	public abstract Staff findStaffByLoginName(String paramString)
			throws Exception;

	public abstract Page<Staff> listStaff(Page<Staff> paramPage)
			throws Exception;

	public abstract void updateStaffRoles(Long paramLong, List<Long> paramList)
			throws Exception;

	public abstract void insertStaffRole(Long paramLong1, Long paramLong2)
			throws Exception;

	public abstract void deleteStaffRole(Long paramLong1, Long paramLong2)
			throws Exception;

	public abstract List<Staff> listDepartmentStaffs(Long paramLong)
			throws Exception;

	public abstract List<Staff> listDepartmentAllStaffs(Long paramLong,
			String paramString1, String paramString2) throws Exception;

	public abstract List<Staff> listStaffs(Long paramLong, String paramString)
			throws Exception;

	public abstract List<Staff> listStaffsByDomain(String paramString1,
			String paramString2) throws Exception;

	public abstract List<Staff> listStaffByRole(Long paramLong)
			throws Exception;

	public abstract Page<Role> listStaffRoles(Long paramLong) throws Exception;

	public abstract void updateStaffDepartment(Long paramLong,
			String paramString) throws Exception;

	public abstract void updateStaffRolesDepartment(Long paramLong,
			String paramString1, String paramString2) throws Exception;

	public abstract List<SecStaffDepartmentRoleKey> listRoleByStaffIds(
			Long paramLong, String paramString) throws Exception;

	public abstract Staff findStaffByMap(Map<String, Object> paramMap);

	public abstract List<StaffExtendProperty> listStaffExtendProperties(
			Long paramLong);

	public abstract void insertStaffExtendProperty(
			StaffExtendProperty paramStaffExtendProperty);
}