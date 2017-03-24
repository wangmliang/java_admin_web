package com.demo.admin.common.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketConfig implements WebSocketConfigurer {

	@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatMessageHandler(),"/websocket.do").addInterceptors(new ChatHandshakeInterceptor());
        registry.addHandler(chatMessageHandler(), "/sockjs/websocket.do").addInterceptors(new ChatHandshakeInterceptor()).withSockJS();
    }
 
    @Bean
    public TextWebSocketHandler chatMessageHandler(){
        return new ChatMessageHandler();
    }

}
