package com.wangml.weixin;

/**
 * 响应消息类型
 * <pre>
 * <b>Title：</b>RespMsgType.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:18:31<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class RespMsgType {

	/**
	 * 返回消息类型：文本
	 */
	public static final String TEXT = "text";

	/**
	 * 返回消息类型：图片
	 */
	public static final String IMAGE = "image";

	/**
	 * 返回消息类型：语音
	 */
	public static final String VOICE = "voice";

	/**
	 * 返回消息类型：视频
	 */
	public static final String VIDEO = "video";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String NEWS = "news";
    
	/**
	 * 返回消息类型：转发到多客服
	 */
	public static final String TRANSFER = "transfer_customer_service";
}
