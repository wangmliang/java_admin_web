package com.wangml.weixin;

/**
 * 
 * <pre>
 * <b>Title：</b>TransferRespMsg.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:19:11<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public final class TransferRespMsg extends BaseRespMsg {

	// 回复的消息内容
	private StringBuilder contentBuilder;

	public String getContent() {
		return contentBuilder.toString();
	}

	public void setContent(String content) {
		contentBuilder = new StringBuilder(content);
	}

	public TransferRespMsg() {
		contentBuilder = new StringBuilder();
	}

	public TransferRespMsg(String content) {
		setContent(content);
	}

	/**
	 * 添加消息内容
	 */
	public TransferRespMsg add(String text) {
		contentBuilder.append(text);
		return this;
	}

	/**
	 * 添加换行符
	 */
	public TransferRespMsg addln() {
		return add("\n");
	}

	/**
	 * 先添加消息内容text，再添加换行符
	 */
	public TransferRespMsg addln(String text) {
		contentBuilder.append(text);
		return addln();
	}

	/**
	 * 添加文本为text,链接为url的超链接. 形如
	 * <p>
	 * &lt;a href=&quot;url&quot;&gt;text&lt;/a&gt;
	 * </p>
	 */
	public TransferRespMsg addLink(String text, String url) {
		contentBuilder.append("<a href=\"").append(url).append("\">")
				.append(text).append("</a>");
		return this;
	}

	@Override
	public String toXml() {
		MsgBuilder mb = new MsgBuilder(super.toXml());
		mb.addData("MsgType", RespMsgType.TRANSFER);
		mb.surroundWith("xml");
		return mb.toString();
	}

	@Override
	public String toString() {
		return "TextMsg [content=" + getContent() + "]";
	}

}
