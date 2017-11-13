package com.wangml.websocket;

import java.util.Date;

/**
 * 传输对象
 * <pre>
 * <b>Title：</b>Message.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年8月28日 - 上午9:23:38<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class Message {

	//发送者
	public Long from;
	//发送者名称
	public String fromName;
	//接收者
	public Long to;
	//发送的文本
	public String text;
	//发送日期
	public Date date;
	/**
	 * 消息标识
	 */
	public String flag;

	public Long getFrom() {
		return from;
	}

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getTo() {
		return to;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "Message [from=" + from + ", fromName=" + fromName + ", to=" + to + ", text=" + text + ", date=" + date + ", flag=" + flag + "]";
	}

}
