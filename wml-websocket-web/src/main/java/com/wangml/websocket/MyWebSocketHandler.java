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
	
	private static Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
	
	// 用于保存HttpSession与WebSocketSession的映射关系
	public static final Map<Long, WebSocketSession> userSocketSessionMap;
	
	/** 账户信息Service */
//	private AccountInfoService accountInfoService;

	static {
		userSocketSessionMap = new ConcurrentHashMap<Long, WebSocketSession>();
	}

	/**
	 * 建立连接后,把登录用户的id写入WebSocketSession
	 */
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//		WebApplicationContext webContext = ContextLoaderListener.getCurrentWebApplicationContext();
//		accountInfoService = (AccountInfoService)webContext.getBean(AccountInfoService.class);
		
		Long id = (Long) session.getAttributes().get("id");
//		AccountInfo user = accountInfoService.findByPrimaryKey(uid);
		if(null != id) {
			if(userSocketSessionMap.get(id) == null || !userSocketSessionMap.get(id).isOpen()) {
				userSocketSessionMap.put(id, session);
				Message msg = new Message();
				msg.setFrom(0L);// 0表示上线消息
				msg.setText(id.toString());
				this.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
			}
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
		/*if (session.isOpen()) {
			session.close();
		}*/
		Long id = (Long)session.getAttributes().get("id");
		Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 移除当前抛出异常用户的Socket会话
		while (it.hasNext()) {
			Entry<Long, WebSocketSession> entry = it.next();
			if (entry.getKey().equals(id)) {
				userSocketSessionMap.remove(entry.getKey());
				logger.debug("Socket会话已经移除:用户ID" + entry.getKey());
//				AccountInfo account = accountInfoService.findByPrimaryKey(entry.getKey());
//				if(null != account) {
					Message msg = new Message();
					msg.setFrom(-2L);
					msg.setText(entry.getKey().toString());
					this.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
//				}
				break;
			}
		}
	}

	/**
	 * 关闭连接后
	 */
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		Long id = (Long)session.getAttributes().get("id");
		logger.debug("Websocket:" + id + "已经关闭");
		Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 移除当前用户的Socket会话
		while (it.hasNext()) {
			Entry<Long, WebSocketSession> entry = it.next();
			if (entry.getKey().equals(id)) {
				userSocketSessionMap.remove(entry.getKey());
				logger.debug("Socket会话已经移除:用户ID" + entry.getKey());
//				AccountInfo account = accountInfoService.findByPrimaryKey(entry.getKey());
//				if(null != account) {
					Message msg = new Message();
					msg.setFrom(-2L);// 下线消息，用-2表示
					msg.setText(entry.getKey().toString());
					this.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
//				}
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
				new Thread(new Runnable() {
					public void run() {
						try {
							if (entry.getValue().isOpen()) {
								entry.getValue().sendMessage(message);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
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
