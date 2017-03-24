package com.aspire.webbas.portal.common.cache;

import com.aspire.webbas.portal.common.entity.OperationAddress;
import java.util.List;
import java.util.Map;

/**
 * 鉴权信息缓存
 * <pre>
 * <b>Title：</b>AuthCache.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:21:03<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class AuthCache {
	/**
	 * 鉴权缓存对象
	 */
	private static AuthCache cache;
	
	/**
	 * 鉴权缓存移除
	 */
	private Map<String, String> unauthMap = null;

	/**
	 * 登录鉴权缓存
	 */
	private Map<String, String> loginAuthMap = null;

	/**
	 * 操作资源缓存
	 */
	private Map<String, List<OperationAddress>> authMap = null;

	public Map<String, String> getUnauthMap() {
		return this.unauthMap;
	}

	public Map<String, String> getLoginAuthMap() {
		return this.loginAuthMap;
	}

	public Map<String, List<OperationAddress>> getAuthMap() {
		return this.authMap;
	}

	public static AuthCache getCache() {
		if (cache == null) {
			cache = new AuthCache();
		}
		return cache;
	}

	/**
	 * 初始化
	 * @param unauth
	 * @param loginAuth
	 * @param auth
	 * @author WML
	 * 2016年11月8日 - 下午5:22:47
	 */
	public void init(Map<String, String> unauth, Map<String, String> loginAuth, Map<String, List<OperationAddress>> auth) {
		this.unauthMap = unauth;
		this.loginAuthMap = loginAuth;
		this.authMap = auth;
	}
}