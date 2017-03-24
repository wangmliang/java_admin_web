<%@page import="com.demo.admin.common.interceptor.ChatMessageHandler"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String username = request.getParameter("username");
	HttpSession session1 = request.getSession(false);
	session1.setAttribute("expId", username);
%>
<html>
<head>
	<title>Java API for WebSocket (JSR-356)</title>  
</head>
<body>
<script src="/wml-admin/assets/js/jquery.min.js"></script>
<script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>
<script type="text/javascript">
	var websocket = null;
	if ('WebSocket' in window) {
		websocket = new WebSocket("ws://192.168.1.170:8080/wml-admin/websocket.do");
	} else if ('MozWebSocket' in window) {
		websocket = new MozWebSocket("ws://192.168.1.170:8080/wml-admin/websocket.do");
	} else {
		websocket = new SockJS("http://192.168.1.170:8080/wml-admin/sockjs/websocket.do");
	}
	websocket.onopen = onOpen;
	websocket.onmessage = onMessage;
	websocket.onerror = onError;
	websocket.onclose = onClose;
	      	
	function onOpen(openEvt) {
		//alert(openEvt.Data);
	}
	
	function onMessage(evt) {
		alert(evt.data);
	}
	function onError() {}
	function onClose() {}
	
	function doSend() {
		if (websocket.readyState == websocket.OPEN) {  		
            var msg = document.getElementById("inputMsg").value;  
            websocket.send(msg);//调用后台handleTextMessage方法
            alert("发送成功!");  
        } else {  
        	alert("连接失败!");  
        }  
	}
</script>
请输入：<textarea rows="5" cols="10" id="inputMsg" name="inputMsg"></textarea>
<button onclick="doSend();">发送</button>
</body>
</html>