package com.wangml.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP请求帮助类
 * 
 * @author Administrator
 */
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
	@SuppressWarnings("deprecation")
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
	@SuppressWarnings("deprecation")
	public final static String sendDataHttps(String data, String url) {
		// HttpClient httpClient = new HttpClient();
		try {
			Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);

			Protocol.registerProtocol("https", myhttps);

			PostMethod httpPost = new PostMethod(url);
			// GetMethod httpGet = new GetMethod(url);
			httpPost.setRequestHeader("content-type", "application/json");
			RequestEntity se = new StringRequestEntity(data);
			httpPost.setRequestEntity(se);

			// httpPost.setRequestHeader("content-type", APPLICATION_JSON);
			// RequestEntity se = new StringRequestEntity(data);
			// httpPost.setRequestEntity(se);

			// int responseCode = httpClient.executeMethod(httpPost);
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
			// Assert.fail(e.getLocalizedMessage());

		}
		return "";
	}

	public static String postByText(String url, Map<String, String> params) throws HttpException, IOException {
		return post(url, params, "text/html;charset=utf-8");
	}

	public static String postByXml(String url, Map<String, String> params) throws HttpException, IOException {
		return post(url, params, "text/xml;charset=utf-8");
	}

	public static String post(String url, Map<String, String> params, String contentType) throws HttpException, IOException {
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

	public static String postByBodyWithCert(String url, String body, String contentType, String certPath) throws HttpException, Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		File file = new File(certPath);
		String fileName = file.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		FileInputStream instream = new FileInputStream(file);
		try {

			keyStore.load(instream, fileName.toCharArray());
		} finally {
			instream.close();
		}
		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, fileName.toCharArray()).build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		StringBuilder sb = new StringBuilder();
		try {

			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", contentType);
			/*
			 * if (StringUtils.isNotBlank(body)) { body = URLEncoder.encode(body, "UTF-8"); }
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
		return sb.toString();
	}

	@SuppressWarnings("deprecation")
	public static String postByBody(String url, String body, String contentType) throws HttpException, IOException {
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", contentType);
		post.setRequestBody(body);
		HttpClient httpClient = new HttpClient();
		httpClient.executeMethod(post);
		String response = post.getResponseBodyAsString();
		return response;
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
			// Assert.fail(e.getLocalizedMessage());
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

	/**
	 * 发起http get请求获取网页源代码
	 * 
	 * @param requestUrl
	 * @return
	 */
	public static String httpRequest(String requestUrl) {
		StringBuffer buffer = null;
		try {
			// 建立连接
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("GET");

			// 获取输入流
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			// 读取返回结果
			buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			httpUrlConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
