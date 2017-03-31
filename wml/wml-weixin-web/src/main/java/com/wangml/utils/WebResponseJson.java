package com.wangml.utils;

/**
 * 响应JSON
 * 
 * <pre>
 * <b>Title：</b>WebResponseJson.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:27:30<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>
 * </pre>
 */
public class WebResponseJson {
	private String code = "00";
	private String msg = "OK";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "WebResponseJson [code=" + code + ", msg=" + msg + "]";
	}

}
