package com.aspire.webbas.portal.common.auth.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;

/**
 * session上下文操作
 * <pre>	
 * Title：SessionContext.java
 * @author：WML
 * @date： 2016年11月7日 - 下午3:04:56
 * @version 1.0
 * @code Copyright (c) 2016 ASPire Tech. 
 * </pre>
 */
public class SessionContext {
	public static String LOGIN_STAFF = "LOGIN_STAFF";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String, HttpSession> sessionMaps = new ConcurrentHashMap();

	private static SessionContext context;

	/**
	 * 获取上下文
	 * @return
	 * @author WML
	 * 2016年11月7日 - 下午3:03:29
	 */
	public static synchronized SessionContext getContext() {
		if (context == null) {
			context = new SessionContext();
		}
		return context;
	}

	/**
	 * 添加session
	 * @param session
	 * @author WML
	 * 2016年11月8日 - 下午4:58:35
	 */
	public void addSession(HttpSession session) {
		sessionMaps.put(session.getId(), session);
	}

	/**
	 * 移除session
	 * @param sessionId
	 * @author WML
	 * 2016年11月8日 - 下午4:58:44
	 */
	public void deleteSession(String sessionId) {
		sessionMaps.remove(sessionId);
	}

	/**
	 * 获取session
	 * @param sessionId
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午4:58:57
	 */
	public HttpSession getSession(String sessionId) {
		return (HttpSession) sessionMaps.get(sessionId);
	}
}