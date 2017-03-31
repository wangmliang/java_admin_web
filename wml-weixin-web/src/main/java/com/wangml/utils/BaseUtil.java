package com.wangml.utils;

import sun.misc.*;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * base64加解密帮助类
 * <pre>
 * <b>Title：</b>BaseUtil.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:04:58<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class BaseUtil {
	private static final Logger log = LoggerFactory.getLogger(BaseUtil.class);

	/**
	 * 加密
	 * @param str
	 * @return
	 * @author WML
	 * 2017年3月31日 - 下午2:05:27
	 */
	@SuppressWarnings("restriction")
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		if (StringUtils.isBlank(str)) {
			return str;
		}
		log.debug("加密值str:=" + str);
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
			log.debug("进来加密:" + s);
		}
		return s;
	}

	/**
	 * 解密
	 * @param s
	 * @return
	 * @author WML
	 * 2017年3月31日 - 下午2:05:31
	 */
	@SuppressWarnings("restriction")
	public static String getFromBase64(String s) {
		if (StringUtils.isBlank(s)) {
			return s;
		}
		byte[] b = null;
		String result = null;
		log.debug("解密值s:=" + s);
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
				log.debug("进来解密:" + result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
