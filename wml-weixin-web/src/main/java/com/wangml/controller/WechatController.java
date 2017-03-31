package com.wangml.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wangml.config.SystemConfig;
import com.wangml.weixin.WechatProcess;

/**
 * 微信入口Controller
 * <pre>
 * <b>Title：</b>WechatController.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:07:28<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
@Controller
@RequestMapping("/wechat")
public class WechatController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WechatController.class);
	
	/**
	 * 微信接口入口
	 * @param request
	 * @param response
	 * @author WML
	 * 2017年3月31日 - 下午2:07:35
	 */
	@RequestMapping("/wechat.do")
	public void wechat(HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isGet = request.getMethod().toLowerCase().equals("get");  
			if(isGet) {
				LOGGER.debug("GET >>>>>>>>>>>");
				// 微信加密签名
		        String signature = request.getParameter("signature");
		        // 时间戮
		        String timestamp = request.getParameter("timestamp");
		        // 随机数
		        String nonce = request.getParameter("nonce");
		        // 随机字符串
		        String echostr = request.getParameter("echostr"); 
		         
		        PrintWriter out = null;
				out = response.getWriter();
				// 通过检验 signature 对请求进行校验，若校验成功则原样返回 echostr，表示接入成功，否则接入失败
				if(checkSignature(signature, timestamp, nonce)){
		           out.print(echostr);
				}
				out.close();
				out = null; 
			} else {
				LOGGER.debug("POST >>>>>>>>>>>");
				wechatInfo(request, response);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e); 
		}
	}
	
    /**
     * 验证签名
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce){
        String[] arr = new String[]{SystemConfig.getInstance().getToken(), timestamp, nonce};
        // 将 token, timestamp, nonce 三个参数进行字典排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for(int i = 0; i < arr.length; i++){
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
         
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行 shal 加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()): false;
    }
     
    /**
     * 将字节数组转换为十六进制字符串
     * @param digest
     * @return
     */
    private static String byteToStr(byte[] digest) {
        String strDigest = "";
        for(int i = 0; i < digest.length; i++){
            strDigest += byteToHexStr(digest[i]);
        }
        return strDigest;
    }
     
    /**
     * 将字节转换为十六进制字符串
     * @param b
     * @return
     */
    private static String byteToHexStr(byte b) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(b >>> 4) & 0X0F];
        tempArr[1] = Digit[b & 0X0F];
        String s = new String(tempArr);
        return s;
    }
	
	
	public void wechatInfo(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        request.setCharacterEncoding("UTF-8");  
        response.setCharacterEncoding("UTF-8");  
  
        /** 读取接收到的xml消息 */  
        StringBuffer sb = new StringBuffer();  
        InputStream is = request.getInputStream();  
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");  
        BufferedReader br = new BufferedReader(isr);  
        String s = "";  
        while ((s = br.readLine()) != null) {  
            sb.append(s);  
        }  
        String xml = sb.toString(); //次即为接收到微信端发送过来的xml数据  
        LOGGER.debug("wechat xml:" + xml);
        String result = "";  
        /** 判断是否是微信接入激活验证，只有首次接入验证时才会收到echostr参数，此时需要把它直接返回 */  
        String echostr = request.getParameter("echostr");  
        if (echostr != null && echostr.length() > 1) {  
            result = echostr;  
        } else {  
            //正常的微信处理流程  
            result = new WechatProcess().processWechatMag(xml);  
        }  
        LOGGER.debug("result >>>>>:" + result);
        try {  
            OutputStream os = response.getOutputStream();  
            os.write(result.getBytes("UTF-8"));  
            os.flush();  
            os.close();  
        } catch (Exception e) {  
            LOGGER.error(e.getMessage(), e); 
        } 
	}
}
