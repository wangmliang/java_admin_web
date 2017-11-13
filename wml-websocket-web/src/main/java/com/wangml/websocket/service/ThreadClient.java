/*
 * ThreadClient.java
 * 深圳市因纳特科技有限公司
 * All rights reserved.
 * -----------------------------------------------
 * 2017年9月27日 下午2:06:13  Created
 * <b>Copyright (c) 2017 ASPire Tech.</b>  
 */
package com.wangml.websocket.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  
 * 客户端操作
 * <pre>
 * <b>Title：</b>ThreadClient.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年9月27日 - 下午2:06:13<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class ThreadClient implements Runnable  {

	private static Logger logger = LoggerFactory.getLogger(ThreadClient.class);
	
	private Socket incoming;
	
	public ThreadClient(Socket s) {
		incoming = s;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("处理线程启动");
			OutputStream o = incoming.getOutputStream();
			PrintWriter p = new PrintWriter(o);
			//这段话就是flash 的 socket 安全策略.
//				p.println("<cross-domain-policy> <site-control permitted-cross-domain-policies=\"all\"/> <allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy> \0");
			p.println("<cross-domain-policy> <site-control permitted-cross-domain-policies=\"all\"/> <allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>");
			p.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				incoming.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
