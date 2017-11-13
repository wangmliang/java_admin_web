/*
 * ThreadPool.java.java
 * 深圳市因纳特科技有限公司
 * All rights reserved.
 * -----------------------------------------------
 * 2017年9月25日 上午9:43:19  Created
 * <b>Copyright (c) 2017 ASPire Tech.</b>  
 */
package com.wangml.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * 线程池操作类
 * <pre>
 * <b>Title：</b>ThreadPool.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年9月25日 - 上午9:43:19<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>
 * </pre>
 */
public class ThreadPool {

	private static ThreadPool threadPool;
	private ThreadPoolTaskExecutor executor = null;
	
	/**
	 * 单例
	 * @return
	 */
	public static ThreadPool init() {
		if (threadPool == null)
			threadPool = new ThreadPool();
		return threadPool;
	}

	/**
	 * 私有构造方法
	 */
	private ThreadPool() {
		// 实现线程池
		WebApplicationContext webContext = ContextLoaderListener.getCurrentWebApplicationContext();
    	executor = (ThreadPoolTaskExecutor) webContext.getBean("taskExecutor");
		System.out.println("线程池初始化成功");
	}

	/**
	 * 线程池获取方法
	 * @return
	 * @author WML
	 * 2017年9月25日 - 上午10:05:43
	 */
	public ThreadPoolTaskExecutor getExecutor() {
		return executor;
	}

	/**
	 * 准备执行 抛入线程池
	 * @param t
	 * @author WML
	 * 2017年9月25日 - 上午10:05:21
	 */
	public void execute(Thread t) {
		executor.execute(t);
	}

	/**
	 * 准备执行 抛入线程池
	 * @param r
	 * @author WML
	 * 2017年9月25日 - 上午10:05:27
	 */
	public void execute(Runnable r) {
		executor.execute(r);
	}

	/**
	 * 返回 Future Future.get()可获得返回结果
	 * @param t
	 * @return
	 * @author WML
	 * 2017年9月25日 - 上午10:05:32
	 */
	public Future<?> submit(Callable<?> t) {
		return getExecutor().submit(t);
	}

	/**
	 * 销毁线程池
	 * @author WML
	 * 2017年9月25日 - 上午10:05:36
	 */
	public void shutdown() {
		getExecutor().shutdown();
	}
}
