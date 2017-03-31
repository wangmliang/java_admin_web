package com.wangml.weixin;

/**
 * 微信xml消息处理流程逻辑类 
 * @author wml
 */
public class WechatProcess {
	/** 
     * 解析处理xml、获取智能回复结果（通过图灵机器人api接口） 
     * @param xml 接收到的微信数据 
     * @return  最终的解析结果（xml格式数据） 
     */  
    public String processWechatMag(String xml){  
        /** 解析xml数据 */  
        ReceiveXmlEntity xmlEntity = new ReceiveXmlProcess().getMsgEntity(xml);  
        BaseRespMsg msg = new BaseRespMsg();// 要发送的消息
        String result = "";
        //请求消息类型：事件推送
    	if(xmlEntity.getMsgType().equals(MsgType.EVENT)) {
    		// 二维码事件
			if (xmlEntity.getTicket() != null) {
				result = "二维码";
			}
			// 关注
			if (xmlEntity.getEvent().equals(MsgType.SUBSCRIBE)) {
				result =  "感谢关注";
			}
			// 取消关注
			else if (xmlEntity.getEvent().equals(MsgType.UNSUBSCRIBE)) {
				result =  "取消关注";
			}
			// 点击菜单拉取消息时的事件推送
			else if (xmlEntity.getEvent().equals(MsgType.CLICK)) {
//				String key = xmlEntity.getEventKey();
				result =  "点击菜单推送";
			}
			// 点击菜单跳转链接时的事件推送
			else if (xmlEntity.getEvent().equals(MsgType.VIEW)) {
				result =  "点击菜单链接跳转";
			}
			// 上报地理位置事件
			else if (xmlEntity.getEvent().equals(MsgType.LOCATION)) {
				result = "上报地理位置";
    		}
    	}
    	// 普通消息
    	else {
    		if (xmlEntity.getMsgType().equals(MsgType.TEXT)) {
				//自动回复（图灵机器人api）
				result = new TulingApiProcess().getTulingResult(xmlEntity.getContent());
			}
			// 图片消息
			else if (xmlEntity.getMsgType().equals(MsgType.IMAGE)) {
				result =  "这是图片消息";
			}
			// 音频消息
			else if (xmlEntity.getMsgType().equals(MsgType.VOICE)) {
				result =  "这是音频消息";
			}
			// 视频消息
			else if (xmlEntity.getMsgType().equals(MsgType.VIDEO)) {
				result =  "这是视频消息";
			}
			// 地理位置消息
			else if (xmlEntity.getMsgType().equals(MsgType.LOCATION)) {
				result =  "这是地理位置消息";
			}
			// 链接消息
			else if (xmlEntity.getMsgType().equals(MsgType.LINK)) {
				result = "这是链接地址";
			}
    	}
    	msg.toXml();
        //返回文本XML
        result = new FormatXmlProcess().formatXmlAnswer(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), result);  
        return result;  
    }  
    
}
