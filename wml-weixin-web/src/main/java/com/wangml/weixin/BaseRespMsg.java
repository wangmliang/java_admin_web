package com.wangml.weixin;

/**
 * 微信响应对象
 * <pre>
 * <b>Title：</b>BaseRespMsg.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:14:00<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>
 * </pre>
 */
public class BaseRespMsg {
	/**
	 * 接收方帐号（收到的OpenID）
	 */
	private String toUserName;
	/**
	 * 开发者微信号
	 */
	private String fromUserName;
	/**
	 * 消息创建时间
	 */
	private long createTime;
	/**
	 * 消息类型
	 */
	private String msgType;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public BaseRespMsg() {
	}

	/**
	 * 将Msg对象转成XML格式
	 */
	public String toXml() {
		MsgBuilder builder = new MsgBuilder(159);
		builder.addData("ToUserName", toUserName);
		builder.addData("FromUserName", fromUserName);
		builder.addTag("CreateTime", String.valueOf(System.currentTimeMillis() / 1000));
		return builder.toString();
	}

	@Override
	public String toString() {
		return toXml();
	}

}
