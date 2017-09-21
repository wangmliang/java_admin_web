package com.wangml.websocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
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
	
	private static Logger logger = LoggerFactory.getLogger(SocketService.class);
 
	public static Object locker = new Object();
	private ServerSocket s = null;
	
	private static ExecutorService executorService = null;// 线程池

	public synchronized void initSocketServer() {
		try {
			if (executorService == null) {
				executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 20);
			} else {
				return;
			}
			s = new ServerSocket(843);
			Runnable r = new Server();
			Thread t = new Thread(r);
			t.start();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void closeServer() {
		locker = null;
		if (s != null && !s.isClosed()) {
			try {
				s.close();
				System.out.println("socket 停止成功");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				System.out.println("socket 停止....");
			}
		}
	}

	class Server implements Runnable {
		@Override
		public void run() {
			try {
				System.out.println("启动ServerSocket..");
				while (locker != null) {
					if (s == null || s.isClosed()) {
						continue;
					}
					Socket incoming = s.accept();
					Runnable r = new ThreadClient(incoming);
					executorService.execute(r);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	class ThreadClient implements Runnable {
		private Socket incoming;

		public ThreadClient(Socket s) {
			incoming = s;
		}

		public void run() {
			try {
				System.out.println("处理线程启动");
				OutputStream o = incoming.getOutputStream();
				PrintWriter p = new PrintWriter(o);
				//这段话就是flash 的 socket 安全策略.做过flex和web交互的同学应该知道
//				p.println("<cross-domain-policy> <site-control permitted-cross-domain-policies=\"all\"/> <allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy> \0");
				p.println("<cross-domain-policy> <site-control permitted-cross-domain-policies=\"all\"/> <allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>");
				p.flush();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				try {
					incoming.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
