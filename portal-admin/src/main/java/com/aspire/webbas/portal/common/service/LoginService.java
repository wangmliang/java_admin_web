package com.aspire.webbas.portal.common.service;

import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.entity.StaffExtendProperty;
import java.util.List;
import javax.servlet.http.HttpSession;

public abstract interface LoginService {
	public static final String LOGIN_NAME = "LOGIN_NAME";
	public static final String REAL_NAME = "REAL_NAME";
	public static final String LOGIN_STAFF = "LOGIN_STAFF";

	public abstract void login(String paramString1, String paramString2,
			HttpSession paramHttpSession) throws Exception;

	public abstract void logout(HttpSession paramHttpSession) throws Exception;

	public abstract boolean isReadContractAgreement(
			List<StaffExtendProperty> paramList);

	public abstract StaffExtendProperty setReadContractAgreement(
			Staff paramStaff);
}