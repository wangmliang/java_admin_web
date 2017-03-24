package com.demo.admin.common.controller.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.demo.common.cache.RedisCacheBasicManager;

/**
 * WEb Socket Server
 * <pre>
 * <b>Title：</b>WebSocketServer.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月3日 - 上午9:18:29<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
@ServerEndpoint(value = "/sockjs/websocket.do", configurator = GetHttpSessionConfigurator.class)
public class CopyOfWebSocketServer {
    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<Map<String, Object>> connections = new CopyOnWriteArraySet<Map<String, Object>>();

    private final String nickname;
//    private Session session;
    
    /**
     * 初始化
     * @author WML
     * 2017年3月3日 - 下午2:26:30
     */
    public CopyOfWebSocketServer() {
    	nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
    	HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        System.out.println("ｗｅｂ　ｓｏｃｋｅｔ　获取ｓｅｓｓｉｏｎ：" + httpSession.getAttribute("expId"));
        int number = 0;
    	Map<String, Object> map = new HashMap<String, Object>();
    	String sessionId = httpSession.getId();
    	System.out.println("sessionId:" + sessionId);
    	if(null != connections) {
    		for (Map<String, Object> client : connections) {
    			if(!client.get("sessionId").equals(sessionId))
    				number++;
        	}
    		if(number == 0) {
    			map.put("sessionId", sessionId);
            	map.put("data", new HashMap<String, Object>().put("name", nickname));
            	connections.add(map);
    		}
    	} else {
    		map.put("sessionId", sessionId);
        	map.put("data", new HashMap<String, Object>().put("name", nickname));
    	}
//    	WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
//    	RedisCacheBasicManager redisCacheBasicManager = (RedisCacheBasicManager) context.getBean("redisCacheBasicManager");
//    	redisCacheBasicManager.set("socket", connections);
    	System.out.println("list size:" + connections.size());
        String message = String.format("* 欢迎：%s %s", nickname, "到来...");
        broadcast(message, session);
    }

    @OnClose
    public void nClose(Session session) throws IOException {
//        connections.remove(this);
        String message = String.format("* %s %s 已离开...", nickname, "has disconnected.");
        session.getBasicRemote().sendText(message);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
    	// TODO: 过滤输入的内容
    	broadcast(message, session);
    }

    @OnError
    public void onError(Throwable t, Session session) throws Throwable {
        session.getBasicRemote().sendText(t.toString());
    }

    private static void broadcast(String msg, Session session) {
        for (Map<String, Object> client : connections) {
            try {
                synchronized (client) {
//                    client.session.getBasicRemote().sendText(msg);
                	session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
            	System.out.println("Chat Error: Failed to send message to client");
                connections.remove(client);
                try {
//                    client.session.close();
                	session.close();
                } catch (IOException e1) {
                    // Ignore
                }
//                String message = String.format("* %s %s", nickname, "has been disconnected.");
                broadcast("Chat Error: Failed to send message to client", session);
            }
        }
    }
}
