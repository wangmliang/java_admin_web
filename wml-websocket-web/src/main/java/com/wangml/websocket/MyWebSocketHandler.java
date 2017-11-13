package com.wangml.websocket;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wangml.util.ThreadPool;

/**
 * Socket处理器
 * <pre>
 * <b>Title：</b>MyWebSocketHandler.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年8月24日 - 上午8:59:32<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
@Component
public class MyWebSocketHandler implements WebSocketHandler {
	
	private static Logger LOG = LoggerFactory.getLogger(MyWebSocketHandler.class);
	
	// 用于保存HttpSession与WebSocketSession的映射关系
	public static Map<Long, WebSocketSession> userSocketSessionMap = new ConcurrentHashMap<Long, WebSocketSession>();
	
	/** 账户信息Service */
//	private AccountInfoService accountInfoService;

	/* static {
		userSocketSessionMap = new ConcurrentHashMap<Long, WebSocketSession>();
	}*/

	/**
	 * 建立连接后,把登录用户的id写入WebSocketSession
	 */
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		/*WebApplicationContext webContext = ContextLoaderListener.getCurrentWebApplicationContext();
		accountInfoService = (AccountInfoService)webContext.getBean(AccountInfoService.class);*/
		
		Long userId = (Long)session.getAttributes().get("userId");
		if(null != userId) {
			if(checkAccount(userId)) {
				LOG.debug("Socket会话已添加:用户ID" + userId);
				userSocketSessionMap.put(userId, session);
			}
		}
	}
	
	/**
	 * 验证是否需要添加
	 * @param userId
	 * @return
	 * @author WML
	 * 2017年10月19日 - 下午5:38:19
	 */
	private boolean checkAccount(Long userId) {
		WebSocketSession webSocket = userSocketSessionMap.get(userId);
		if(webSocket == null) {
			return true;
		} else {
			return webSocket.isOpen() ? false : true;
		}
	}

	/**
	 * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
	 */
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if (message.getPayloadLength() == 0)
			return;
		Message msg = new Gson().fromJson(message.getPayload().toString(), Message.class);
		msg.setDate(new Date());
		sendMessageToUser(msg.getTo(), new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
	}

	/**
	 * 消息传输错误处理
	 */
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		Long userId = (Long)session.getAttributes().get("userId");
		Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 移除当前抛出异常用户的Socket会话
		while (it.hasNext()) {
			Entry<Long, WebSocketSession> entry = it.next();
			if (entry.getKey().equals(userId)) {
				userSocketSessionMap.remove(entry.getKey());
				LOG.debug("Socket会话已经移除:用户ID" + entry.getKey());
				break;
			}
		}
	}

	/**
	 * 关闭连接后
	 */
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
//		Long id = (Long)session.getAttributes().get("userId");
		Long userId = (Long)session.getAttributes().get("userId");
		LOG.debug("Websocket:" + userId + "已经关闭");
		Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 移除当前用户的Socket会话
		while (it.hasNext()) {
			Entry<Long, WebSocketSession> entry = it.next();
			if (entry.getKey().equals(userId)) {
				userSocketSessionMap.remove(entry.getKey());
				LOG.debug("Socket会话已经移除:用户ID" + entry.getKey());
				break;
			}
		}
	}

	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给所有在线用户发送消息
	 * @param message
	 * @throws IOException
	 * @author WML
	 * 2017年8月29日 - 上午11:17:16
	 */
	public void broadcast(final TextMessage message) throws IOException {
		Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 多线程群发
		while (it.hasNext()) {
			final Entry<Long, WebSocketSession> entry = it.next();
			if (entry.getValue().isOpen()) {
				executeThread(entry, message);
			}
		}
	}
	
	/**
	 * 线程发送消息
	 * @param entry
	 * @param message
	 * @author WML
	 * 2017年10月19日 - 下午5:42:25
	 */
	private void executeThread(final Entry<Long, WebSocketSession> entry, final TextMessage message) {
		ThreadPool.init().execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (entry.getValue().isOpen()) {
						entry.getValue().sendMessage(message);
					}
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		});
	}

	/**
	 * 给某个用户发送消息
	 * @param uid
	 * @param message
	 * @throws IOException
	 * @author WML
	 * 2017年8月29日 - 上午11:17:24
	 */
	public void sendMessageToUser(Long uid, TextMessage message) throws IOException {
		WebSocketSession session = userSocketSessionMap.get(uid);
		if (session != null && session.isOpen()) {
			session.sendMessage(message);
		}
	}
	
}
