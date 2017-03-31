package com.wangml.weixin;

/**
 * 微信消息类型
 * <pre>
 * <b>Title：</b>MsgType.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:16:26<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class MsgType {
	/**
	 * 请求消息类型：文本
	 */
	public static final String TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String VOICE = "voice";

	/**
	 * 请求消息类型：视频
	 */
	public static final String VIDEO = "video";

	/**
	 * 请求消息类型：事件推送
	 */
	public static final String EVENT = "event";
	
	/**
	 * 事件类型：subscribe(1、订阅；2、扫描带参数二维码事件，用户未关注时，进行关注后的事件推送)
	 */
	public static final String SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(点击菜单拉取消息时的事件推送)
	 */
	public static final String CLICK = "CLICK";

	/**
	 * 事件类型：VIEW(点击菜单跳转链接时的事件推送)
	 */
	public static final String VIEW = "VIEW";

	/**
	 * 事件类型：SCAN(扫描带参数二维码事件，用户已关注时的事件推送)
	 */
	public static final String SCAN = "SCAN";
}
