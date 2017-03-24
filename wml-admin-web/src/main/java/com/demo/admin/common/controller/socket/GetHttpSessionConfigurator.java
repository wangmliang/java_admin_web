package com.demo.admin.common.controller.socket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.springframework.stereotype.Component;

/**
 * Web Socket 获取HttpSession
 * <pre>
 * <b>Title：</b>GetHttpSessionConfigurator.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月3日 - 下午2:50:35<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
@Component
public class GetHttpSessionConfigurator extends Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession=(HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}
