package com.wangml.listener;

import javax.servlet.ServletContextEvent;

import com.wangml.websocket.SocketService;

/**
 * IE8兼容Socket监听
 * <pre>
 * <b>Title：</b>ServletContextListener.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年9月21日 - 上午9:03:32<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class ServletContextListener implements javax.servlet.ServletContextListener {
	
	/**
	 * 这个socket主要还是为了 flash的socket 
	 */
	private SocketService service;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		service.closeServer();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		service = new SocketService();
		service.initSocketServer();
	}

}
