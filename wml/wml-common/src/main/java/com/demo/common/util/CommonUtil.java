package com.demo.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 公用方法工具类
 * <pre>
 * <b>Title：</b>CommonUtil.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月27日 下午2:07:12<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 *  </pre>
 */
public class CommonUtil {
    
	/**
	 * 获取ip
	 * @param request
	 * @return
	 * @author WML
	 * 2016年10月27日 - 下午1:58:34
	 */
	public static String getIp(HttpServletRequest request) {
		if (request == null)
			return "";
		String ip = request.getHeader("X-Requested-For");
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 截取第一个，号前面的IP
	 * @param ip   IP字符串串
	 * @return ip
	 * @author WML
	 * 2016年10月27日 - 下午1:58:10
	 */
	public static String splitIp(String ip){
		String strIp = "";
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(ip)){
			String[] ips = ip.split(",");
			for (int i = 0; i < ips.length; i++) {
				if(i == 0){
					strIp = ips[0];
				}
			}
			return strIp;
		}else{
			return "";
		}
	}
}
