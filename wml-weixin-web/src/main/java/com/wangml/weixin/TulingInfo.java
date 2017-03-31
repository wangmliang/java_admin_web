/*
 * TulingInfo.java.java
 * 深圳市因纳特科技有限公司
 * All rights reserved.
 * -----------------------------------------------
 * 2017年3月31日 下午2:34:25  Created
 * <b>Copyright (c) 2017 ASPire Tech.</b>  
 */
package com.wangml.weixin;

/**
 * 图灵机器人请求返回
 * <pre>
 * <b>Title：</b>TulingInfo.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:34:25<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>
 * </pre>
 */
public class TulingInfo {
	private Integer code;
	private String text;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "TulingInfo [code=" + code + ", text=" + text + "]";
	}

}
