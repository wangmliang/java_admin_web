/*
 * UserDto.java.java
 * 深圳市因纳特科技有限公司
 * All rights reserved.
 * -----------------------------------------------
 * 2017年8月24日 上午11:08:55  Created
 * <b>Copyright (c) 2017 ASPire Tech.</b>  
 */
package com.wangml.websocket;

/**  
 * 
 * <pre>
 * <b>Title：</b>UserDto.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年8月24日 - 上午11:08:55<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class UserDTO {
	
	/***
	 * 用户id
	 */
	private Long userId;
	
	/**
	 * 用户昵称
	 */
	private String userName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", userName=" + userName + "]";
	}
	
}
