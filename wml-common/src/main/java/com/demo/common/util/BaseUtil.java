package com.demo.common.util;

import sun.misc.*; 

import java.io.UnsupportedEncodingException;



import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base 加解密工具类
 * <pre>
 * <b>Title：</b>BaseUtil.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月27日 下午1:57:04<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 *  </pre>
 */
@SuppressWarnings("restriction")
public class BaseUtil {

	private static final Logger log = LoggerFactory.getLogger(BaseUtil.class);
	
    // 加密  
	public static String getBase64(String str) {  
        byte[] b = null;  
        String s = null;
        if(StringUtils.isBlank(str)){
        	return str;
        }
        log.debug("加密值str:="+str);
        try {  
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        if (b != null) {  
            s = new BASE64Encoder().encode(b);
            log.debug("进来加密:"+s);
        }  
        return s;  
    }  
  
    // 解密  
    public static String getFromBase64(String s) {
    	if(StringUtils.isBlank(s)){
        	return s;
        }
        byte[] b = null;  
        String result = null;
        log.debug("解密值s:="+s);
        if (s != null) {  
            BASE64Decoder decoder = new BASE64Decoder();  
            try {  
                b = decoder.decodeBuffer(s);  
                result = new String(b, "utf-8");
                log.debug("进来解密:"+result);
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }
	    
    public static void main(String[] args) {
//	    	MyGreetingCard my=new MyGreetingCard();
//	    	my.setNickName("sss");
//	    	System.out.println(my.getNickName());
//	    	System.out.println(URLDecoder.decode("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0399890de5406af4&redirect_uri=http%3A%2F%2Ffmp.szwisdom.cn%2Ffmp-act%2FgreetingCard%2F151104662874345206%2Fca62d9cdb3d3426396919033769d5c25%2Fcallback.do&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect "));
//			String nickeName[]={"黄先森","风轻云淡","胖胖更健康","如海","陈华权","小顿","巍言怂听","Snow","廖坚强","AJ","慢慢走远","陈文阳","谭樯","A 曾召国","吕永明","考拉","在路上","贾健","如海","那年的我","jilly","晚风逐月"};
//			for(int i=0;i<nickeName.length;i++){
//				String str="update my_greeting_card set nick_name='"+getBase64(nickeName[i])+"' where nick_name='"+nickeName[i]+"';";
//				System.out.println(str);
//			}
		
//			String nickNames[]={"巍言怂听","Snow","陈华权","廖坚强","胖胖更健康","黄先森","如海","陈文阳","谭樯","在路上","jilly"};
//			for(int i=0;i<nickNames.length;i++){
//				String str="update wx_pay_record set nick_name='"+getBase64(nickNames[i])+"' where nick_name='"+nickNames[i]+"';";
//				System.out.println(str);
//			}
		
		
//			String nickNameb[]={"风轻云淡","那年的我","Tony Qin","pzy","芳?","vivian","巍言怂听","Snow","陈华权","廖坚强","胖胖更健康","黄先森","如海","陈文阳","谭樯","在路上","jilly","小顿 ","如海","AJ","慢慢走远","吕永明","考拉","Taoge","贾健","Elva★紫藤","刘健","jojo"};
//			for(int i=0;i<nickNameb.length;i++){
//				String str="update campaign_reward_info set nick_name='"+getBase64(nickNameb[i])+"' where nick_name='"+nickNameb[i]+"';";
//				System.out.println(str);
//			}
    }
}
