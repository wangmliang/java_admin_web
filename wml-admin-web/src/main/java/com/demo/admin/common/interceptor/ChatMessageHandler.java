package com.demo.admin.common.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatMessageHandler extends TextWebSocketHandler {
	
	// 这个会出现性能问题，最好用Map来存储，key用userid
	private static final Map<String, Object> users;
	private static Logger logger = Logger.getLogger(ChatMessageHandler.class);
	
	static {
//		WebSocketSession
		users = new HashMap<String, Object>();
	}

	public ChatMessageHandler() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 连接成功时候，会触发UI上onopen方法
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("connect to the websocket success......");
		users.put("", session);
		// 这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
		// TextMessage returnMessage = new TextMessage("你将收到的离线");
		// session.sendMessage(returnMessage);
	}

	/**
	 * 在UI在用js调用websocket.send()时候，会调用该方法
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);

	}

	/**
	 * 给某个用户发送消息
	 * @param userName
	 * @param message
	 */
	public void sendMessageToUser(String userName, TextMessage message) {
//		for (WebSocketSession user : users) {
//		for (String user : users.keySet()) {
		for (Entry<String, Object> entry : users.entrySet()) {
			WebSocketSession user = (WebSocketSession) entry.getValue();
			if (user.getAttributes().get("webSocketName").equals(userName)) {
				try {
					if (user.isOpen()) {
						user.sendMessage(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	/**
	 * 给所有在线用户发送消息
	 *
	 * @param message
	 */
	public void sendMessageToUsers(TextMessage message) {
//		for (WebSocketSession user : users) {
		for (Entry<String, Object> entry : users.entrySet()) {
			WebSocketSession user = (WebSocketSession) entry.getValue();
			try {
				if (user.isOpen()) {
					user.sendMessage(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		logger.debug("websocket connection closed......");
		users.remove(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		logger.debug("websocket connection closed......");
		users.remove(session);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
