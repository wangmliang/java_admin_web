<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.getSession().setAttribute("expId", 1);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="UTF-8">
    <title>Landing Page | Amaze UI Example</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <script src="/wml-admin/assets/js/jquery.min.js"></script>
    <!-- 妹子UI相关资源 -->
<!--     <script src="assets/js/amazeui.min.js"></script> -->
    <!-- UM相关资源 -->
<!--     <link href="assets/umeditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet"> -->
<!--     <script type="text/javascript" charset="utf-8" src="assets/umeditor/umeditor.config.js"></script> -->
<!--     <script type="text/javascript" charset="utf-8" src="assets/umeditor/umeditor.min.js"></script> -->
<!--     <script type="text/javascript" src="assets/umeditor/lang/zh-cn/zh-cn.js"></script> -->
    
<!--     <link rel="alternate icon" type="image/png" href="//s.amazeui.org/assets/2.x/i/favicon.png"> -->
<!--     <link rel="apple-touch-icon-precomposed" href="//s.amazeui.org/assets/2.x/i/app-icon72x72@2x.png"> -->
<!--     <meta name="apple-mobile-web-app-capable" content="yes"> -->
<!--     <meta name="apple-mobile-web-app-status-bar-style" content="black"> -->
<!--     <link rel="stylesheet" href="//s.amazeui.org/assets/2.x/css/amazeui.min.css?v=ivnem2cy"> -->
<!--     <link rel="stylesheet" href="//s.amazeui.org/assets/2.x/css/amaze.min.css?v=ivnem2cy"> -->
<!--     <link rel="canonical" href="http://amazeui.org/widgets/navbar/default/0?_ver=2.x"> -->
<!--     <script src="//s.amazeui.org/assets/2.x/js/jquery.min.js?v=ivnem2cy"></script> -->
<!--     <script src="//s.amazeui.org/assets/2.x/js/handlebars.min.js?v=ivnem2cy"></script> -->
<!--     <script src="//s.amazeui.org/assets/2.x/js/amazeui.min.js?v=ivnem2cy"></script> -->
<!--     <script type="text/javascript" src="http://static.duoshuo.com/embed.js" charset="UTF-8"></script> -->
<!--     <link type="text/css" rel="stylesheet" href="http://static.duoshuo.com/styles/embed.default.css?9b2a46a0.css"> -->
</head>
<body>
<head>
    <title>Apache Tomcat WebSocket Examples: Chat</title>
    <style type="text/css">
/*     <![CDATA[ */
        input#chat {
            width: 410px
        }

        #console-container {
            width: 400px;
        }

        #console {
            border: 1px solid #CCCCCC;
            border-right-color: #999999;
            border-bottom-color: #999999;
            height: 170px;
            overflow-y: scroll;
            padding: 5px;
            width: 100%;
        }

        #console p {
            padding: 0;
            margin: 0;
        }
/*     ]]> */
    </style>
    <script type="application/javascript">
        var Chat = {};

        Chat.socket = null;

        Chat.connect = (function(host) {
            if ('WebSocket' in window) {
                Chat.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
                Chat.socket = new MozWebSocket(host);
            } else {
                Console.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            Chat.socket.onopen = function () {
                Console.log('Info: WebSocket connection opened.');
                document.getElementById('chat').onkeydown = function(event) {
                    if (event.keyCode == 13) {
                        Chat.sendMessage();
                    }
                };
            };

            Chat.socket.onclose = function () {
                document.getElementById('chat').onkeydown = null;
                Console.log('Info: WebSocket closed.');
            };

            Chat.socket.onmessage = function (message) {
                Console.log(message.data);
            };
        });

        Chat.initialize = function() {
            if (window.location.protocol == 'http:') {
                Chat.connect('ws://' + window.location.host + '/wml-admin/websocket');
            } else {
                Chat.connect('wss://' + window.location.host + '/wml-admin/websocket');
            }
        };

        Chat.sendMessage = (function() {
            var message = document.getElementById('chat').value;
            if (message != '') {
                Chat.socket.send(message);
                document.getElementById('chat').value = '';
            }
        });

        var Console = {};

        Console.log = (function(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
            while (console.childNodes.length > 25) {
                console.removeChild(console.firstChild);
            }
            console.scrollTop = console.scrollHeight;
        });

        Chat.initialize();

        document.addEventListener("DOMContentLoaded", function() {
            // Remove elements with "noscript" class - <noscript> is not allowed in XHTML
            var noscripts = document.getElementsByClassName("noscript");
            for (var i = 0; i < noscripts.length; i++) {
                noscripts[i].parentNode.removeChild(noscripts[i]);
            }
        }, false);
    </script>
</head>
<body>
<div class="noscript">
	<h2 style="color: #ff0000">
		Seems your browser doesn't support Javascript! Websockets rely on Javascript being enabled. Please enable Javascript and reload this page!
	</h2>
</div>
<div>
    <p>
    	<input type="text" placeholder="type and press enter to chat" id="chat" />
    </p>
    <div id="console-container">
        <div id="console"></div>
    </div>
</div>
</body>
</html>