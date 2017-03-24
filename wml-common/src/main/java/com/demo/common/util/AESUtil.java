package com.demo.common.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <pre>
 * <b>Title：</b>AESUtil.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月27日 下午1:54:25<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 *  </pre>
 */
public class AESUtil {
    
	private static final Logger LOG = LoggerFactory.getLogger(AESUtil.class);

	/**
	 * 加密
	 * @param content  待加密内容
	 * @param password 加密的密钥
	 * @return 加密密文
	 * @author WML
	 * 2016年10月27日 - 下午1:54:47
	 */
	public static String encrypt_ex(String content, String password) {
		SecureRandom random = null;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}

		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// kgen.init(128, new SecureRandom(password.getBytes()));
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				String hex = Integer.toHexString(result[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				sb.append(hex.toUpperCase());
			}
			return sb.toString(); // 加密
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			LOG.error(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			LOG.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			LOG.error(e.getMessage(), e);
		} catch (BadPaddingException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 解密
	 * @param cipherContent    解密密文
	 * @param password         秘钥
	 * @return 解密字符串
	 * @author WML
	 * 2016年10月27日 - 下午1:55:19
	 */
	public static String decrypt_ex(String cipherContent, String password) {
		if (cipherContent.length() < 1)
			return null;
		byte[] content = new byte[cipherContent.length() / 2];
		for (int i = 0; i < cipherContent.length() / 2; i++) {
			int high = Integer.parseInt(cipherContent.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(cipherContent.substring(i * 2 + 1, i * 2 + 2), 16);
			content[i] = (byte) (high * 16 + low);
		}
		SecureRandom random = null;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// kgen.init(128, new SecureRandom(password.getBytes()));
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return new String(result); // 加密
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			LOG.error(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			LOG.error(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			LOG.error(e.getMessage(), e);
		} catch (BadPaddingException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 加密
	 * @param input    待加密字符串
	 * @param o_key    秘钥
	 * @return 加密密文
	 * @author WML
	 * 2016年10月27日 - 下午1:55:48
	 */
	public static String encrypt(String input, String o_key) {
		byte[] crypted = null;
		String key = o_key;
		if (key.length() > 16) {
			key = key.substring(0, 16);
		} else if (key.length() < 16) {
			int paddingLen = 16 - key.length();
			for (int i = 0; i < paddingLen; i++)
				key = key + "0";
		}
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			crypted = cipher.doFinal(input.getBytes());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return new String(Base64.encodeBase64(crypted));
	}

	/**
	 * 解密
	 * @param input    解密密文
	 * @param o_key    秘钥
	 * @return 解密字符串
	 * @author WML
	 * 2016年10月27日 - 下午1:56:13
	 */
	public static String decrypt(String input, String o_key) {
		byte[] output = null;
		String key = o_key;
		if (key.length() > 16) {
			key = key.substring(0, 16);
		} else if (key.length() < 16) {
			int paddingLen = 16 - key.length();
			for (int i = 0; i < paddingLen; i++)
				key = key + "0";
		}
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			output = cipher.doFinal(Base64.decodeBase64(input.getBytes()));
			return new String(output);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	public static void main(String[] args) {
		String key = "13802298818";
		String data = "rG+Pgknoltd00LODtQ6njmbmiT51mCHZtCgPAqNXW6+oYr1/1jPN5R+KvKePGVjyMmBJn5+cHkQrSe6g6WLJgk1J1ntVgPo+8W7ti26UvunvnAM3t2s25HetQA7QcgSm3y2omHak4RNXwoPFGqNTwn2c04TEGlRfJPTGMVrWBDDpkqj8elw+FGALBNjHEHuYYxhMudqXtdSGeRPoNNuTsbtkKC/Us3ru4i/+YRvNr1R+t1Dwvl0jhRdgrnjFpBuN7wHnWGiRqjt17R+T8t+M5yuUrI3MK2BMHdO8QR78zjdnMND+6Ijl1HDTGu5QMlu2PPR9xqbOEoXaiiAuUrslJvkypmS6GfHuyEg39IVl/CZvyOZcn/SCnYf2+A+ERaNVXd2Ibt5vVa/Il11EbgAyig==";
		System.out.println(AESUtil.decrypt(data, key));
		// System.out.println(AESUtil.decrypt(AESUtil.encrypt(data, key), key));
	}
}
