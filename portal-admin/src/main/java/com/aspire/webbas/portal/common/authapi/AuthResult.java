package com.aspire.webbas.portal.common.authapi;

import java.io.Serializable;

/**
 * 鉴权返回
 * <pre>
 * <b>Title：</b>AuthResult.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:01:16<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class AuthResult implements Serializable {
	private static final long serialVersionUID = -4078036728685021925L;
	
	/**
	 * 成功状态码 
	 */
	public static String SUCCESS = "0";
	
	/**
	 * 失败状态码 
	 */
	public static String FAIL = "1";
	
	/**
	 * 无效状态码 
	 */
	public static String INVALID = "2";
	
	/**
	 * 未登录状态码 
	 */
	public static String NOT_LOGIN = "3";
	
	/**
	 * 返回状态码
	 */
	private String returnCode;
	
	/**
	 * 返回信息
	 */
	private String message;
	
	/**
	 * 重定向url
	 */
	private String redirectUrl;

	public AuthResult() {
	}

	public AuthResult(String code, String message, String url) {
		this.returnCode = code;
		this.message = message;
		this.redirectUrl = url;
	}

	public String getRedirectUrl() {
		return this.redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getReturnCode() {
		return this.returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}