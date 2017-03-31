package com.wangml.weixin;

/**
 * StringBuilder的包装类，用于构建微信消息
 * <pre>
 * <b>Title：</b>MsgBuilder.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:16:10<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class MsgBuilder {

	private StringBuilder builder;

	public MsgBuilder() {
		builder = new StringBuilder();
	}

	public MsgBuilder(int capacity) {
		builder = new StringBuilder(capacity);
	}

	public MsgBuilder(String str) {
		builder = new StringBuilder(str);
	}

	public void append(String str) {
		builder.append(str);
	}

	public void insert(String str) {
		builder.insert(0, str);
	}

	/**
	 * 将现有的XML文档以tag标签包围
	 * 
	 * @param tag
	 *            标签名
	 */
	public void surroundWith(String tag) {
		StringBuilder sb = new StringBuilder(builder.capacity() + tag.length() * 2 + 5);
		sb.append("<").append(tag).append(">\n").append(builder).append("</").append(tag).append(">\n");
		builder = sb;
	}

	public void addTag(String tagName, String text) {
		if (text == null) {
			return;
		}
		builder.append("<").append(tagName).append(">").append(text).append("</").append(tagName).append(">\n");
	}

	public void addData(String tagName, String data) {
		if (data == null) {
			return;
		}
		builder.append("<").append(tagName).append("><![CDATA[").append(data).append("]]></").append(tagName).append(">\n");
	}

	@Override
	public String toString() {
		return builder.toString();
	}

}
