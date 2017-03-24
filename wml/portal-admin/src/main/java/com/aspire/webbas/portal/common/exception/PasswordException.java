package com.aspire.webbas.portal.common.exception;

/**
 * 密码操作异常
 * <pre>
 * <b>Title：</b>PasswordException.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:53:47<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class PasswordException extends RuntimeException {
	private static final long serialVersionUID = -3924220709786144773L;

	public PasswordException() {
		
	}

	public PasswordException(String message) {
		super(message);
	}

	public PasswordException(String message, Throwable cause) {
		super(message, cause);
	}
}