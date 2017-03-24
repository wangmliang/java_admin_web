package com.aspire.webbas.portal.common.util;

import com.aspire.webbas.portal.common.exception.PasswordException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.validator.GenericValidator;

import sun.misc.BASE64Encoder;

/**
 * 密码操作类 Title：PasswordUtil.java
 * 
 * @author：WML
 * @date： 2016年11月7日 - 下午2:11:14
 * @version 1.0
 * @code Copyright (c) 2016 ASPire Tech.
 */
@SuppressWarnings({ "unused", "restriction" })
public final class PasswordUtil {

	private static final String CHAR_NUMBER = "0123456789";
	private static final String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
	private static final String CHAR_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String CHAR_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String CAHR_SPECIAL = "?!@#+=%^*";
	private static final byte[] normalChars = { 65, 66, 67, 68, 69, 70, 71, 72,
			73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89,
			90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109,
			110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 };

	private static final byte[] digitChars = { 48, 49, 50, 51, 52, 53, 54, 55,
			56, 57 };

	private static final char[] specialChars = { '+', '-', '*', '/', ',', '.',
			'@', '_', '=', '>', '<', '?', '#', '$', '%', '&', '(', ')', '!',
			'~' };
	private static final String ALGORITHM_PBEWithMD5AndDES = "PBEWithMD5AndDES";
	private static final byte[] SALT = { 65, 82, 103, 54, 48, 83, 99, 71 };

	private static long seed = System.currentTimeMillis();

	private static int passwordMinLength = 6;
	private static int passwordMaxLength = 20;
	private static String passwordRule = "NONE";

	public static String buildPassword(String staffId, String password)
			throws PasswordException {
		verifyPasswordLength(password);
		verifyPasswordComplex(password);
		return cryptPassword(staffId, password);
	}

	public static String buildPasswordWithoutVerify(String staffId,
			String password) {
		return cryptPassword(staffId, password);
	}

	public static String cryptPassword(String key, String password) {
		PBEKeySpec publicKey = null;
		SecretKey secretKey = null;
		PBEParameterSpec parameter = null;

		String encoding = "GBK";
		try {
			publicKey = new PBEKeySpec(key.toCharArray());

			SecretKeyFactory factory = SecretKeyFactory
					.getInstance("PBEWithMD5AndDES");

			secretKey = factory.generateSecret(publicKey);

			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

			parameter = new PBEParameterSpec(SALT, 1000);

			cipher.init(1, secretKey, parameter);

			byte[] passwordByte = password.getBytes(encoding);

			byte[] encrypedPasswordByte = cipher.doFinal(passwordByte);

			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] digistPasswordByte = md.digest(encrypedPasswordByte);

			BASE64Encoder encoder = new BASE64Encoder();

			String encyrpedPassword = encoder.encode(digistPasswordByte);

			return encyrpedPassword.replace('\r', ' ').replace('\n', ' ')
					.replaceAll(" ", "");
		} catch (NoSuchAlgorithmException e) {
			throw new PasswordException("加密算法：PBEWithMD5AndDES不存在！", e);
		} catch (InvalidKeySpecException e) {
			throw new PasswordException("公钥" + publicKey.toString() + "不正确！", e);
		} catch (NoSuchPaddingException e) {
			throw new PasswordException("不支持PBEWithMD5AndDES加密算法的Padding！", e);
		} catch (InvalidKeyException e) {
			throw new PasswordException("加密算法：PBEWithMD5AndDES的私钥" + secretKey
					+ "不正确！", e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new PasswordException("加密算法：PBEWithMD5AndDES的参数" + parameter
					+ "不正确！", e);
		} catch (UnsupportedEncodingException e) {
			throw new PasswordException("不支持" + encoding + "编码！", e);
		} catch (IllegalBlockSizeException e) {
			throw new PasswordException("加密块大小不正确！", e);
		} catch (BadPaddingException e) {
			throw new PasswordException("加密填充异常！", e);
		}
	}

	public static boolean verifyPasswordLength(String password)
			throws PasswordException {
		if (password == null) {
			return false;
		}
		if (password.length() < passwordMinLength) {
			throw new PasswordException("密码长度不能小于" + passwordMinLength + "位");
		}
		if (password.length() > passwordMaxLength) {
			throw new PasswordException("密码长度不能大于" + passwordMaxLength + "位");
		}
		return true;
	}

	public static boolean verifyPasswordComplex(String password)
			throws PasswordException {
		if (password == null) {
			return false;
		}
		boolean result = false;

		// switch (1.$SwitchMap$com$aspire$webbas$portal$common$util$PasswordUtil$PasswordComplex[PasswordComplex.valueOf(passwordRule).ordinal()])
		switch (PasswordComplex.valueOf(passwordRule).ordinal()) {
		case 1:
			if ((GenericValidator.matchRegexp(password, "^[a-zA-Z0-9]+$"))
					&& (GenericValidator.matchRegexp(password, "[a-zA-Z]+"))
					&& (GenericValidator.matchRegexp(password, "[0-9]+"))) {
				result = true;
			} else {
				throw new PasswordException("您的密码不符合复杂度规则，密码必须由数字和字母组成，请修改密码");
			}
			break;
		case 2:
			if ((GenericValidator.matchRegexp(password, "[a-zA-Z]+"))
					&& (GenericValidator.matchRegexp(password, "[0-9]+"))) {
				result = true;
			} else {
				throw new PasswordException("您的密码不符合复杂度规则，密码必须同时包含数字和字母，请修改密码");
			}
			break;
		case 3:
			if ((GenericValidator.matchRegexp(password, "[0-9]+"))
					&& (GenericValidator.matchRegexp(password, "[a-zA-Z]+"))
					&& (GenericValidator.matchRegexp(password, "[^a-zA-Z0-9]+"))) {
				result = true;
			} else {
				throw new PasswordException(
						"您的密码不符合复杂度规则，密码必须同时包含数字、字母和特殊字符，请修改密码");
			}
			break;
		default:
			result = true;
		}
		return result;
	}

	public static String generatePassword() {
		char[] chars = new char[passwordMinLength];

		int r = 0;
		switch (PasswordComplex.valueOf(passwordRule).ordinal()) {
		case 4:
			for (int i = 0; i < passwordMinLength; i++) {
				r = getRandom(0,
						"0123456789abcdefghijklmnopqrstuvwxyz".length() - 1);
				chars[i] = "0123456789abcdefghijklmnopqrstuvwxyz".charAt(r);
			}
			break;
		case 1:
			if (passwordMinLength < 2) {
				throw new RuntimeException("简单强度的密码最小长度应该大于等于2。当前设置"
						+ passwordMinLength);
			}
			r = getRandom(0, "0123456789".length() - 1);
			chars[0] = "0123456789".charAt(r);
			r = getRandom(0,
					"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
							.length() - 1);
			chars[1] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
					.charAt(r);
			for (int i = 2; i < passwordMinLength; i++) {
				r = getRandom(0,
						"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
								.length() - 1);
				chars[i] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
						.charAt(r);
			}
			break;
		case 2:
			if (passwordMinLength < 2) {
				throw new RuntimeException("复杂强度的密码最小长度应该大于等于2。当前设置"
						+ passwordMinLength);
			}
			r = getRandom(0, "0123456789".length() - 1);
			chars[0] = "0123456789".charAt(r);
			r = getRandom(0,
					"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
							.length() - 1);
			chars[1] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
					.charAt(r);
			for (int i = 2; i < passwordMinLength; i++) {
				r = getRandom(0,
						"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ?!@#+=%^*"
								.length() - 1);
				chars[i] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ?!@#+=%^*"
						.charAt(r);
			}
			break;
		case 3:
			if (passwordMinLength < 3) {
				throw new RuntimeException("非常复杂强度的密码最小长度应该大于等于3。当前设置"
						+ passwordMinLength);
			}
			r = getRandom(0, "0123456789".length() - 1);
			chars[0] = "0123456789".charAt(r);
			r = getRandom(0,
					"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
							.length() - 1);
			chars[1] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
					.charAt(r);
			r = getRandom(0, "?!@#+=%^*".length() - 1);
			chars[2] = "?!@#+=%^*".charAt(r);
			for (int i = 3; i < passwordMinLength; i++) {
				r = getRandom(0,
						"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ?!@#+=%^*"
								.length() - 1);
				chars[i] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ?!@#+=%^*"
						.charAt(r);
			}
		}
		return mix(chars);
	}

	public static String autoGeneratePassword() {
		Random random = new Random();

		int passwordLen = passwordMinLength
				+ random.nextInt(passwordMaxLength - passwordMinLength);

		int digitsize = passwordLen / 3;

		int specialsize = passwordLen / 3;

		String strPassword = "";
		if (PasswordComplex.valueOf(passwordRule) == PasswordComplex.NONE) {
			passwordRule = "SIMPLE";
		}

		switch (PasswordComplex.valueOf(passwordRule).ordinal()) {
		case 1:
			for (int i = 0; i < passwordLen - digitsize; i++) {
				strPassword = strPassword
						+ (char) normalChars[random.nextInt(normalChars.length)];
			}
			for (int i = 0; i < digitsize; i++)
				strPassword = strPassword
						+ (char) digitChars[random.nextInt(digitChars.length)];
			break;
		case 2:
			for (int i = 0; i < passwordLen - digitsize - specialsize; i++) {
				strPassword = strPassword
						+ (char) normalChars[random.nextInt(normalChars.length)];
			}
			for (int i = 0; i < digitsize; i++) {
				strPassword = strPassword
						+ (char) digitChars[random.nextInt(digitChars.length)];
			}
			for (int i = 0; i < specialsize; i++)
				strPassword = strPassword
						+ specialChars[random.nextInt(specialChars.length)];
			break;
		case 3:
			for (int i = 0; i < passwordLen - digitsize - specialsize; i++) {
				strPassword = strPassword
						+ (char) normalChars[random.nextInt(normalChars.length)];
			}
			for (int i = 0; i < digitsize; i++) {
				strPassword = strPassword
						+ (char) digitChars[random.nextInt(digitChars.length)];
			}
			for (int i = 0; i < specialsize; i++) {
				strPassword = strPassword
						+ specialChars[random.nextInt(specialChars.length)];
			}
		}
		return mix(strPassword.toCharArray());
	}

	private static String mix(char[] chars) {
		char[] newchars = new char[chars.length];
		for (int i = 0; i < newchars.length; i++) {
			newchars[i] = ' ';
		}
		for (int i = 0; i < chars.length; i++) {
			int r = getRandom(0, newchars.length - 1);
			while (newchars[r] != ' ') {
				r = getRandom(0, newchars.length - 1);
			}
			newchars[r] = chars[i];
		}
		return new String(newchars);
	}

	private static int getRandom(int min, int max) {
		Random random = new Random(seed);
		seed += random.nextLong();

		return min + random.nextInt(max - min + 1);
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			// printHelp();
			// System.exit(-1);
		}
		try {
			String staffId = "wf";
			String pwd = "123456";
			// 6ktKmidBw7iWfRTAuhT+6A==
			String crypPwd = cryptPassword(staffId, pwd);
			System.out.println("加密后的密码：" + crypPwd);
		} catch (Exception e) {
			System.out.println("生产加密后的密码出错");
			System.out.println(e);
			System.exit(-1);
		}
	}

	private static void printHelp() {
		StringBuffer buf = new StringBuffer();
		buf.append("用法:\n passwordGenerator.bat <用户ID> <未加密密码>\n");
		buf.append("例如:\n");
		buf.append("PasswordUtil.bat user01 user001\n");
		System.out.println(buf.toString());
	}

	public static enum PasswordComplex {
		NONE, SIMPLE, COMPLEX, VERY_COMPLEX;

		public String getValue() {
			return toString();
		}
	}
}