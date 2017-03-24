package com.demo.admin.common.controller.socket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import com.demo.admin.common.interceptor.ChatMessageHandler;

@Controller
public class WebsocketController {
	
	@Bean//这个注解会从Spring容器拿出Bean
	public ChatMessageHandler chatMessageHandler() {
		return new ChatMessageHandler();
	}
	
	@RequestMapping("/websocket/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		HttpSession session = request.getSession(false);
		session.setAttribute("sessionName", username);
//		response.sendRedirect("/ship/websocket/ws.jsp");
		return new ModelAndView("pages/socket/index2.jsp");
	}

	@RequestMapping("/websocket/send")
	@ResponseBody
	public String send(HttpServletRequest request) {
		String username = request.getParameter("username");
		chatMessageHandler().sendMessageToUser(username, new TextMessage("你好，测试！！！！"));
		return null;
	}
}
