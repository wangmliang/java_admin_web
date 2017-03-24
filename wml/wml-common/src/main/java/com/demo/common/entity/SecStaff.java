/*
 * SecStaff.java
 * 因纳特科技有限公司
 * All rights reserved.
 * -----------------------------------------------
 * 2016-10-31 15:37:54  Created
 */
package com.demo.common.entity;

import java.util.Date;

/**
 * 成员
 * <pre>
 * <b>Title：</b>SecStaff.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@version：</b>@version V1.0<br/>
 * <b>@date：</b>2016-10-31 15:37:54 Created<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class SecStaff {
	
	/**
	 * 成员ID
	 */
	private Long staffId;
	
	/**
	 * 登录名
	 */
	private String loginName;
	
	/**
	 * 组织ID
	 */
	private Long departmentId;
	
	/**
	 * 成员姓名
	 */
	private String realName;
	
	/**
	 * 密码（经过加密）
	 */
	private String password;
	
	/**
	 * 成员状态
	 */
	private String status;
	
	/**
	 * 性别：MALE-男性；FEMALE-女性；
	 */
	private String sex;
	
	/**
	 * 电话
	 */
	private String telephone;
	
	/**
	 * 手机号码
	 */
	private String mobile;
	
	/**
	 * 邮件地址
	 */
	private String email;
	
	/**
	 * 成员创建者
	 */
	private String createUser;
	
	/**
	 * 成员创建时间
	 */
	private Date createDate;
	
	/**
	 * 成员帐号过期时间
	 */
	private Date expireDate;
	
	/**
	 * 成员最后修改时间
	 */
	private Date lastUpdateDate;
	
	/**
	 * 密码失效时间
	 */
	private Date passwordExpireDate;
	
	/**
	 * 用户锁定时间
	 */
	private Date lockDate;
	
	/**
	 * 成员所在城市（参见CITY表）
	 */
	private Integer cityId;
	
	/**
     * 设置：成员ID
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.STAFF_ID
     */
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	
	/**
     * 获取：成员ID
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.STAFF_ID
     */
	public Long getStaffId() {
		return staffId;
	}
	
	/**
     * 设置：登录名
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.LOGIN_NAME
     */
	public void setLoginName(String loginName) {
		this.loginName = loginName == null ? null : loginName.trim();
	}
	
	/**
     * 获取：登录名
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.LOGIN_NAME
     */
	public String getLoginName() {
		return loginName;
	}
	
	/**
     * 设置：组织ID
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.DEPARTMENT_ID
     */
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	
	/**
     * 获取：组织ID
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.DEPARTMENT_ID
     */
	public Long getDepartmentId() {
		return departmentId;
	}
	
	/**
     * 设置：成员姓名
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.REAL_NAME
     */
	public void setRealName(String realName) {
		this.realName = realName == null ? null : realName.trim();
	}
	
	/**
     * 获取：成员姓名
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.REAL_NAME
     */
	public String getRealName() {
		return realName;
	}
	
	/**
     * 设置：密码（经过加密）
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.PASSWORD
     */
	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}
	
	/**
     * 获取：密码（经过加密）
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.PASSWORD
     */
	public String getPassword() {
		return password;
	}
	
	/**
     * 设置：成员状态
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.STATUS
     */
	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}
	
	/**
     * 获取：成员状态
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.STATUS
     */
	public String getStatus() {
		return status;
	}
	
	/**
     * 设置：性别：MALE-男性；FEMALE-女性；
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.SEX
     */
	public void setSex(String sex) {
		this.sex = sex == null ? null : sex.trim();
	}
	
	/**
     * 获取：性别：MALE-男性；FEMALE-女性；
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.SEX
     */
	public String getSex() {
		return sex;
	}
	
	/**
     * 设置：电话
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.TELEPHONE
     */
	public void setTelephone(String telephone) {
		this.telephone = telephone == null ? null : telephone.trim();
	}
	
	/**
     * 获取：电话
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.TELEPHONE
     */
	public String getTelephone() {
		return telephone;
	}
	
	/**
     * 设置：手机号码
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.MOBILE
     */
	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}
	
	/**
     * 获取：手机号码
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.MOBILE
     */
	public String getMobile() {
		return mobile;
	}
	
	/**
     * 设置：邮件地址
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.EMAIL
     */
	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}
	
	/**
     * 获取：邮件地址
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.EMAIL
     */
	public String getEmail() {
		return email;
	}
	
	/**
     * 设置：成员创建者
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.CREATE_USER
     */
	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}
	
	/**
     * 获取：成员创建者
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.CREATE_USER
     */
	public String getCreateUser() {
		return createUser;
	}
	
	/**
     * 设置：成员创建时间
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.CREATE_DATE
     */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	/**
     * 获取：成员创建时间
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.CREATE_DATE
     */
	public Date getCreateDate() {
		return createDate;
	}
	
	/**
     * 设置：成员帐号过期时间
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.EXPIRE_DATE
     */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	/**
     * 获取：成员帐号过期时间
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.EXPIRE_DATE
     */
	public Date getExpireDate() {
		return expireDate;
	}
	
	/**
     * 设置：成员最后修改时间
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.LAST_UPDATE_DATE
     */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	/**
     * 获取：成员最后修改时间
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.LAST_UPDATE_DATE
     */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	/**
     * 设置：密码失效时间
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.PASSWORD_EXPIRE_DATE
     */
	public void setPasswordExpireDate(Date passwordExpireDate) {
		this.passwordExpireDate = passwordExpireDate;
	}
	
	/**
     * 获取：密码失效时间
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.PASSWORD_EXPIRE_DATE
     */
	public Date getPasswordExpireDate() {
		return passwordExpireDate;
	}
	
	/**
     * 设置：用户锁定时间
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.LOCK_DATE
     */
	public void setLockDate(Date lockDate) {
		this.lockDate = lockDate;
	}
	
	/**
     * 获取：用户锁定时间
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.LOCK_DATE
     */
	public Date getLockDate() {
		return lockDate;
	}
	
	/**
     * 设置：成员所在城市（参见CITY表）
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @param id the value for sec_staff.CITY_ID
     */
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	
	/**
     * 获取：成员所在城市（参见CITY表）
     * @author WML
     * @version V1.0 
     * 2016-10-31 15:37:54
     * @return the value of sec_staff.CITY_ID
     */
	public Integer getCityId() {
		return cityId;
	}
	
	@Override
    public String toString() {
    	return "SecStaff [staffId=" +  staffId + ", loginName=" +  loginName + ", departmentId=" +  departmentId + ", realName=" +  realName 
		+ ", password=" +  password + ", status=" +  status + ", sex=" +  sex 
		+ ", telephone=" +  telephone + ", mobile=" +  mobile + ", email=" +  email 
		+ ", createUser=" +  createUser + ", createDate=" +  createDate + ", expireDate=" +  expireDate 
		+ ", lastUpdateDate=" +  lastUpdateDate + ", passwordExpireDate=" +  passwordExpireDate + ", lockDate=" +  lockDate 
		+ ", cityId=" +  cityId + "]";
    }
    
}