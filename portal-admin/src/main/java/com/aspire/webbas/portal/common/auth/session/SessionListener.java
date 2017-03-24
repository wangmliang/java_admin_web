package com.aspire.webbas.portal.common.auth.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * session����
 * <pre>
 * <b>Title��</b>SessionListener.java<br/>
 * <b>@author��</b>WML<br/>
 * <b>@date��</b>2016��11��8�� - ����4:59:06<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class SessionListener implements HttpSessionListener {
	
	/**
	 * ��¼session����ʱ
	 */
	public void sessionCreated(HttpSessionEvent event) {
		System.out.println("SessionListener:����session��" + event.getSession().getId() + "����context�С�");
		SessionContext.getContext().addSession(event.getSession());
	}

	/**
	 * ��¼session�Ƴ�ʱ
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		System.out.println("SessionListener:��context��ɾ��session��" + event.getSession().getId() + "����");
		SessionContext.getContext().deleteSession(event.getSession().getId());
	}
}