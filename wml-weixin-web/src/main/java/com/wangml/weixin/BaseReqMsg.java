package com.wangml.weixin;

/**
 * 
 * <pre>
 * <b>Title：</b>BaseReqMsg.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:14:09<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>
 * </pre>
 */
public class BaseReqMsg {

	String toUserName;// 开发者微信号
	String fromUserName;// 发送方帐号（一个OpenID）
	long createTime;// 消息创建时间
	String msgType;// 消息类型
	String msgId;// 消息id，64位整型

	/**
	 * 得到开发者微信号
	 */
	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	/**
	 * 得到发送方帐号（一个OpenID）
	 */
	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	/**
	 * 得到消息创建时间
	 */
	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * 得到消息类型
	 */
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	/**
	 * 得到消息id
	 */
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * 将ReqMsg对象转成XML格式
	 */
	public String toXml() {
		// 159 = 106 + 28(ToUserName) + 15(FromUserName) + 10(CreateTime)
		MsgBuilder builder = new MsgBuilder(159);
		builder.addData("ToUserName", toUserName);
		builder.addData("FromUserName", fromUserName);
		builder.addTag("CreateTime", String.valueOf(createTime));
		return builder.toString();
	}

}
