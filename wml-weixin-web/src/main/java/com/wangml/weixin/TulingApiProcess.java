package com.wangml.weixin;

import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangml.config.SystemConfig;

/**
 * 调用图灵机器人api接口，获取智能回复内容
 * 
 * <pre>
 * <b>Title：</b>TulingApiProcess.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:21:32<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>
 * </pre>
 */
public class TulingApiProcess {

	private static final Logger LOGGER = LoggerFactory.getLogger(TulingApiProcess.class);

	/**
	 * 调用图灵机器人api接口，获取智能回复内容，解析获取自己所需结果
	 * 
	 * @param content
	 * @return
	 */
	public String getTulingResult(String content) {
		String apiKey = SystemConfig.getInstance().getApiKey();
		/** 此处为图灵api接口，参数key需要自己去注册申请，先以11111111代替 */
		String apiUrl = "http://www.tuling123.com/openapi/api?key=" + apiKey + "&info=";
		String param = "";
		try {
			param = apiUrl + URLEncoder.encode(content, "utf-8");
		} catch (Exception e) {
			LOGGER.error("图灵地址转化异常：" + e.getMessage(), e);
		} // 将参数转为url编码

		/** 发送httpget请求 */
		HttpGet request = new HttpGet(param);
		String result = "";
		try {
			HttpResponse response = HttpClients.createDefault().execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			LOGGER.error("图灵请求返回异常：" + e.getMessage(), e);
		}
		/** 请求失败处理 */
		if (null == result) {
			return "对不起，你说的话真是太高深了……";
		}

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			TulingInfo tuling = objectMapper.readValue(result, TulingInfo.class);
			// 以code=100000为例，参考图灵机器人api文档
			if (null != tuling && 100000 == tuling.getCode()) {
				result = tuling.getText();
			} else {
				LOGGER.error("图灵机器人请求返回错误！code=" + tuling.getCode() + ",text=" + tuling.getText());
				result = "对不起，你说的话真是太高深了……";
			}
		} catch (Exception e) {
			LOGGER.error("图灵返回信息转化：" + e.getMessage(), e);
		}
		return result;
	}

	public static void main(String[] args) {
		String result = new TulingApiProcess().getTulingResult("你好");
		System.out.println(result);
	}
}
