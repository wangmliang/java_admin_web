package com.wangml.mq.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("clientPushListener")
public class ClientPushListener implements MessageListener {
	 protected static final Logger logger = Logger.getLogger(ClientPushListener.class);
	@Override
	public void onMessage(Message message) {
		 logger.info("[ClientPushListener.onMessage]:begin onMessage.");
	        TextMessage textMessage = (TextMessage) message;
	        try {
	            String jsonStr = textMessage.getText();
	            logger.info("[ClientPushListener.onMessage]:receive message is,"+ jsonStr);
	        } catch (JMSException e) {
	            logger.error("[ClientPushListener.onMessage]:receive message occured an exception",e);
	        }
	        logger.info("[ClientPushListener.onMessage]:end onMessage.");
	    }
}

