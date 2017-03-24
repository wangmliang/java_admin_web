package com.aspire.webbas.portal.common.service;

import java.util.List;
import java.util.Map;

public abstract interface AuthService {
	public abstract Map<String, String> listAddressUrlByLoginName(
			String paramString) throws Exception;

	public abstract boolean authorizeSuccess(String paramString,
			Map<String, String> paramMap);

	public abstract boolean authorizeFail(String paramString,
			Map<String, String> paramMap);

	public abstract boolean authExclude(String paramString);

	public abstract boolean notNeedAuthAfterLogin(String paramString);

	public abstract boolean authorize(Long paramLong, String paramString);

	public abstract List<Map<String, Boolean>> authorize(Long paramLong,
			String[] paramArrayOfString1, String[] paramArrayOfString2);
}