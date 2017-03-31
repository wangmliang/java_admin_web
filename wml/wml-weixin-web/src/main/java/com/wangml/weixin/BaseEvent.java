package com.wangml.weixin;

/**
 * 
 * <pre>
 * <b>Title：</b>BaseEvent.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:14:13<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>
 * </pre>
 */
public class BaseEvent extends BaseReqMsg {

	private String event;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public BaseEvent() {
		setMsgType(RequestMsgType.EVENT);
	}

	@Override
	public String toXml() {
		MsgBuilder mb = new MsgBuilder(super.toXml());
		mb.addData("MsgType", RequestMsgType.EVENT);
		mb.addData("Event", event);
		return mb.toString();
	}

}
