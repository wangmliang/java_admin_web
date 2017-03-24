package com.aspire.webbas.portal.common.entity;

public class SecStaffDepartmentRoleKey {
	private String staffId;
	private String departmentId;
	private Long roleId;

	public String getStaffId() {
		return this.staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = (staffId == null ? null : staffId.trim());
	}

	public String getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = (departmentId == null ? null : departmentId.trim());
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}