package com.wangml.websocket.service;

import java.io.IOException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wangml.util.ThreadPool;

/** 
 * 这个socket主要还是为了 flash的socket
 * <pre>
 * <b>Title：</b>SocketService.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年9月21日 - 上午9:01:43<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class SocketService {
	
	private static Logger LOG = LoggerFactory.getLogger(SocketService.class);
 
	private ServerSocket s = null;
	
	public static Object locker = new Object();
	/**
	 * 初始化SocketService 
	 * @author WML
	 * 2017年9月22日 - 上午8:37:53
	 */
	public synchronized void initSocketServer() {
		try {
			s = new ServerSocket(843);
			Runnable r = new ServerThread(s);
			ThreadPool.init().execute(r);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * 关闭服务
	 * @author WML
	 * 2017年9月22日 - 上午8:38:13
	 */
	public void closeServer() {
		if(locker != null) {
			locker = null;
		}
		if (s != null && !s.isClosed()) {
			try {
				s.close();
				LOG.debug("socket 停止成功");
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			} finally {
				LOG.debug("socket 停止......");
			}
		}
	}
}
