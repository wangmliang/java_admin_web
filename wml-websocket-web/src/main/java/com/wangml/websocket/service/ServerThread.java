/*
 * ServerThread.java
 * 深圳市因纳特科技有限公司
 * All rights reserved.
 * -----------------------------------------------
 * 2017年9月27日 下午2:05:17  Created
 * <b>Copyright (c) 2017 ASPire Tech.</b>  
 */
package com.wangml.websocket.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wangml.util.ThreadPool;

/**  
 * ServerSocket操作类
 * <pre>
 * <b>Title：</b>ServerThread.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年9月27日 - 下午2:05:17<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class ServerThread implements Runnable {
	
	private static Logger LOG = LoggerFactory.getLogger(ServerThread.class);

	private ServerSocket s;
	
	public ServerSocket getS() {
		return s;
	}

	public void setS(ServerSocket s) {
		this.s = s;
	}

	/**
	 * 构造方法
	 * @param locker
	 * @author WML
	 * 2017年9月27日 - 下午2:10:06
	 */
	public ServerThread(ServerSocket s) {
		this.s = s;
	}
	
	@Override
	public void run() {
		Socket socket = null;
		try {
			LOG.debug("启动ServerSocket..");
			while (SocketService.locker != null) {
				if (s == null || s.isClosed()) {
					continue;
				}
				socket = s.accept();
				Runnable r = new ThreadClient(socket);
				ThreadPool.init().getExecutor().execute(r);
			}
		} catch (Exception e) {
			if(socket != null) {
				try {
					socket.close();
				} catch (IOException e1) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}

}
