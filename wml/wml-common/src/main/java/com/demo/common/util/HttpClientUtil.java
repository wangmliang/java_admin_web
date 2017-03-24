package com.demo.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP请求帮助类
 * <pre>
 * <b>Title：</b>HttpClientUtil.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月27日 下午2:01:17<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 *  </pre>
 */
@SuppressWarnings("deprecation")
public class HttpClientUtil {

	private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);

	private static final String APPLICATION_JSON = "application/json";

	@SuppressWarnings("unused")
	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	private static final String UTF_8 = "UTF-8";

	/**
	 * 往指定的URL发送数据。（JSON）
	 * 
	 * @param data
	 * @param url
	 * @return
	 */
	public final static String sendData(String data, String url) {
		HttpClient httpClient = new HttpClient();

		PostMethod httpPost = new PostMethod(url);
		httpPost.setRequestHeader("content-type", APPLICATION_JSON);
		RequestEntity se = new StringRequestEntity(data);
		httpPost.setRequestEntity(se);
		try {

			int responseCode = httpClient.executeMethod(httpPost);
			if (responseCode == 200) {
				byte[] resBody = httpPost.getResponseBody();

				String responseString = "";
				if (null == resBody || 0 == resBody.length) {
					responseString = httpPost.getResponseBodyAsString();
				} else {
					responseString = new String(resBody, UTF_8);
				}
				return responseString;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 往指定的URL发送数据。（JSON+HTTPS）
	 * 
	 * @param data
	 * @param url
	 * @return
	 */
	public final static String sendDataHttps(String data, String url) {
		try {
			Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);

			Protocol.registerProtocol("https", myhttps);

			PostMethod httpPost = new PostMethod(url);

			httpPost.setRequestHeader("content-type", "application/json");
			RequestEntity se = new StringRequestEntity(data);
			httpPost.setRequestEntity(se);

			byte[] resBody = httpPost.getResponseBody();

			String responseString = "";
			if (null == resBody || 0 == resBody.length) {
				responseString = httpPost.getResponseBodyAsString();
			} else {
				responseString = new String(resBody, UTF_8);
			}
			return responseString;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String postByText(String url, Map<String, String> params)
			throws HttpException, IOException {
		return post(url, params, "text/html;charset=utf-8");
	}

	public static String postByXml(String url, Map<String, String> params)
			throws HttpException, IOException {
		return post(url, params, "text/xml;charset=utf-8");
	}

	public static String post(String url, Map<String, String> params,
			String contentType) throws HttpException, IOException {
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", contentType);
		for (String key : params.keySet()) {
			post.setParameter(key, params.get(key));
		}
		HttpClient httpClient = new HttpClient();
		httpClient.executeMethod(post);
		String response = post.getResponseBodyAsString();
		return response;
	}

	public static String postByBodyWithCert(String url, String body,
			String contentType, String certPath, String muchId) throws HttpException, Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		File file = new File(certPath);
		String fileName = file.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		//证书路径 如：/xxx/xxx/apiclient_cert.p12
		FileInputStream instream = new FileInputStream(file);
		try {
			/**
			 * instream : 文件输入流
			 * muchId.toCharArray()： 默认商户id
			 */
			keyStore.load(instream, muchId.toCharArray());
		} finally {
			instream.close();
		}
		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, muchId.toCharArray()).build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		StringBuilder sb = new StringBuilder();
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", contentType);
			/*
			 * if (StringUtils.isNotBlank(body)) { body =
			 * URLEncoder.encode(body, "UTF-8"); }
			 */
			HttpEntity se = new StringEntity(body, "UTF-8");
			httpPost.setEntity(se);

			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String text;

					while ((text = bufferedReader.readLine()) != null) {
						sb.append(text);
					}
				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return new String(sb.toString().getBytes(), UTF_8);
//		return sb.toString();
	}

	public static String postByBody(String url, String body, String contentType)
			throws HttpException, IOException {
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", contentType);
		post.setRequestBody(body);
		HttpClient httpClient = new HttpClient();
		httpClient.executeMethod(post);
		byte[] resBody = post.getResponseBody();
		String responseString = "";
		if (null == resBody || 0 == resBody.length) {
			responseString = post.getResponseBodyAsString();
		} else {
			responseString = new String(resBody, UTF_8);
		}
		return responseString;
	}
	
	/**
	 * 微信下单post请求
	 * @param url
	 * @param body
	 * @return
	 * @author WML
	 * 2016年6月14日-下午4:54:05
	 */
	public static String sendOrderPost(String url, String body) {
		 HttpPost post = new HttpPost(url);   
	     StringEntity entity = new StringEntity(body,"UTF-8");
	     entity.setContentEncoding("utf-8");
	     entity.setContentType("text/xml");
	     post.setEntity(entity);
	     try {
			return EntityUtils.toString(new DefaultHttpClient().execute(post).getEntity(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	     return "";
	}

	@SuppressWarnings("unused")
	public final static String sendDataHttpsViaGet(String url) {
		LOG.debug("进来httpS");
		HttpClient httpClient = new HttpClient();
		if (url.startsWith("https")) {

		} else {
			return sendDataHttpViaGet(url);
		}
		try {
			Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);

			Protocol.registerProtocol("https", myhttps);

			GetMethod httpGet = new GetMethod(url);
			httpGet.setRequestHeader("content-type", "application/json");
			httpGet.setRequestHeader("X-FORWARDED-FOR", "183.61.189.134");

			int responseCode = httpClient.executeMethod(httpGet);
			byte[] resBody = httpGet.getResponseBody();

			String responseString = "";
			if (null == resBody || 0 == resBody.length) {
				responseString = httpGet.getResponseBodyAsString();
			} else {
				responseString = new String(resBody, "UTF-8");
			}
			LOG.debug("Http url=" + url);
			LOG.debug("Http get ret=" + responseString);
			return responseString;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return "";
	}

	@SuppressWarnings("unused")
	public final static String sendDataHttpViaGet(String url) {
		HttpClient httpClient = new HttpClient();

		try {
			GetMethod httpGet = new GetMethod(url);
			httpGet.setRequestHeader("content-type", "application/json");

			int responseCode = httpClient.executeMethod(httpGet);
			byte[] resBody = httpGet.getResponseBody();

			String responseString = "";
			if (null == resBody || 0 == resBody.length) {
				responseString = httpGet.getResponseBodyAsString();
			} else {
				responseString = new String(resBody, "UTF-8");
			}
			LOG.debug("Http url=" + url);
			LOG.debug("Http get ret=" + responseString);
			return responseString;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
