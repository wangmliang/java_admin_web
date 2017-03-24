package com.aspire.webbas.portal.common.config;

/**
 * 常量类
 * <pre>
 * <b>Title：</b>Constants.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:25:07<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class Constants {
	/**
	 * ticket_cookie_name
	 */
	public static final String TICKET_COOKIE_NAME = "AUTH_TICKET";
	
	/**
	 * cookie有效用户名
	 */
	public static final String COOKIE_VALID_USERNAME = "username";
	
	/**
	 * cookie有效密码
	 */
	public static final String COOKIE_VALID_PASSWD = "token";
	
	/**
	 * cookie最大有效时间 
	 */
	public static final int COOKIE_VALID_MAXAGE = 259200;
}