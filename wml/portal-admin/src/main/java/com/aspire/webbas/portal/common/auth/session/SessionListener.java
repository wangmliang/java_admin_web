package com.aspire.webbas.portal.common.auth.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * session监听
 * <pre>
 * <b>Title：</b>SessionListener.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午4:59:06<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class SessionListener implements HttpSessionListener {
	
	/**
	 * 记录session创建时
	 */
	public void sessionCreated(HttpSessionEvent event) {
		System.out.println("SessionListener:保存session【" + event.getSession().getId() + "】到context中。");
		SessionContext.getContext().addSession(event.getSession());
	}

	/**
	 * 记录session移除时
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		System.out.println("SessionListener:从context中删除session【" + event.getSession().getId() + "】。");
		SessionContext.getContext().deleteSession(event.getSession().getId());
	}
}