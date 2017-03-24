package com.aspire.webbas.portal.common.util;

import com.aspire.webbas.configuration.config.ConfigurationHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import javax.crypto.Cipher;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class RSAUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtil.class);
	private static final String ALGORITHOM = "RSA";
	private static final String RSA_PAIR_FILENAME = "/__RSA_PAIR.txt";
	private static final int KEY_SIZE = 1024;
	private static KeyPairGenerator keyPairGen = null;
	private static KeyFactory keyFactory = null;

	private static KeyPair oneKeyPair = null;

	private static File rsaPairFile = new File(getRSAPairFilePath());

	private static synchronized KeyPair generateKeyPair() {
		try {
			keyPairGen.initialize(1024, new SecureRandom(DateFormatUtils.format(new Date(), "yyyyMMdd").getBytes()));
			oneKeyPair = keyPairGen.generateKeyPair();
			saveKeyPair(oneKeyPair);
			return oneKeyPair;
		} catch (InvalidParameterException ex) {
			LOGGER.error("KeyPairGenerator does not support a key length of 1024.", ex);
		} catch (NullPointerException ex) {
			LOGGER.error("RSAUtils#KEY_PAIR_GEN is null, can not generate KeyPairGenerator instance.", ex);
		}

		return null;
	}

	private static String getRSAPairFilePath() {
		String urlPath = ConfigurationHelper.getBasePath();
		return urlPath + "/__RSA_PAIR.txt";
	}

	private static boolean isCreateKeyPairFile() {
		boolean createNewKeyPair = false;
		if ((!rsaPairFile.exists()) || (rsaPairFile.isDirectory())) {
			createNewKeyPair = true;
		}
		return createNewKeyPair;
	}

	private static void saveKeyPair(KeyPair keyPair) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = FileUtils.openOutputStream(rsaPairFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(keyPair);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(fos);
		}
	}

	public static KeyPair getKeyPair() {
		if (isCreateKeyPairFile()) {
			return generateKeyPair();
		}
		if (oneKeyPair != null) {
			return oneKeyPair;
		}
		return readKeyPair();
	}

	private static KeyPair readKeyPair() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = FileUtils.openInputStream(rsaPairFile);
			ois = new ObjectInputStream(fis);
			oneKeyPair = (KeyPair) ois.readObject();
			return oneKeyPair;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(ois);
			IOUtils.closeQuietly(fis);
		}
		return null;
	}

	public static RSAPublicKey generateRSAPublicKey(byte[] modulus,
			byte[] publicExponent) {
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(
				modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
		} catch (InvalidKeySpecException ex) {
			LOGGER.error("RSAPublicKeySpec is unavailable.", ex);
		} catch (NullPointerException ex) {
			LOGGER.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
		}
		return null;
	}

	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus,
			byte[] privateExponent) {
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(
				new BigInteger(modulus), new BigInteger(privateExponent));
		try {
			return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
		} catch (InvalidKeySpecException ex) {
			LOGGER.error("RSAPrivateKeySpec is unavailable.", ex);
		} catch (NullPointerException ex) {
			LOGGER.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
		}
		return null;
	}

	public static RSAPrivateKey getRSAPrivateKey(String hexModulus,
			String hexPrivateExponent) {
		if ((StringUtils.isBlank(hexModulus))
				|| (StringUtils.isBlank(hexPrivateExponent))) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("hexModulus and hexPrivateExponent cannot be empty. RSAPrivateKey value is null to return.");
			}
			return null;
		}
		byte[] modulus = null;
		byte[] privateExponent = null;
		try {
			modulus = Hex.decodeHex(hexModulus.toCharArray());
			privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
		} catch (DecoderException ex) {
			LOGGER.error("hexModulus or hexPrivateExponent value is invalid. return null(RSAPrivateKey).");
		}
		if ((modulus != null) && (privateExponent != null)) {
			return generateRSAPrivateKey(modulus, privateExponent);
		}
		return null;
	}

	public static RSAPublicKey getRSAPublidKey(String hexModulus,
			String hexPublicExponent) {
		if ((StringUtils.isBlank(hexModulus))
				|| (StringUtils.isBlank(hexPublicExponent))) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("hexModulus and hexPublicExponent cannot be empty. return null(RSAPublicKey).");
			}
			return null;
		}
		byte[] modulus = null;
		byte[] publicExponent = null;
		try {
			modulus = Hex.decodeHex(hexModulus.toCharArray());
			publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
		} catch (DecoderException ex) {
			LOGGER.error("hexModulus or hexPublicExponent value is invalid. return null(RSAPublicKey).");
		}
		if ((modulus != null) && (publicExponent != null)) {
			return generateRSAPublicKey(modulus, publicExponent);
		}
		return null;
	}

	public static byte[] encrypt(PublicKey publicKey, byte[] data)
			throws Exception {
		Cipher ci = Cipher.getInstance("RSA");
		ci.init(1, publicKey);
		return ci.doFinal(data);
	}

	public static byte[] decrypt(PrivateKey privateKey, byte[] data)
			throws Exception {
		Cipher ci = Cipher.getInstance("RSA");
		ci.init(2, privateKey);
		return ci.doFinal(data);
	}

	public static String encryptString(PublicKey publicKey, String plaintext) {
		if ((publicKey == null) || (plaintext == null)) {
			return null;
		}
		byte[] data = plaintext.getBytes();
		try {
			byte[] en_data = encrypt(publicKey, data);
			return new String(Hex.encodeHex(en_data));
		} catch (Exception ex) {
			LOGGER.error(ex.getCause().getMessage());
		}
		return null;
	}

	public static String encryptString(String plaintext) {
		if (plaintext == null) {
			return null;
		}
		byte[] data = plaintext.getBytes();
		KeyPair keyPair = getKeyPair();
		try {
			byte[] en_data = encrypt((RSAPublicKey) keyPair.getPublic(), data);
			return new String(Hex.encodeHex(en_data));
		} catch (NullPointerException ex) {
			LOGGER.error("keyPair cannot be null.");
			ex.printStackTrace();
		} catch (Exception ex) {
			LOGGER.error(ex.getCause().getMessage());
			ex.printStackTrace();
		}
		return null;
	}

	public static String decryptString(PrivateKey privateKey, String encrypttext) {
		if ((privateKey == null) || (StringUtils.isBlank(encrypttext)))
			return null;
		try {
			byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
			byte[] data = decrypt(privateKey, en_data);
			return new String(data);
		} catch (Exception ex) {
			LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s",
					new Object[] { encrypttext, ex.getCause().getMessage() }));
		}
		return null;
	}

	public static String decryptString(String encrypttext) {
		if (StringUtils.isBlank(encrypttext)) {
			return null;
		}
		KeyPair keyPair = getKeyPair();
		try {
			byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
			byte[] data = decrypt((RSAPrivateKey) keyPair.getPrivate(), en_data);
			return new String(data);
		} catch (NullPointerException ex) {
			LOGGER.error("keyPair cannot be null.");
		} catch (Exception ex) {
			LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s",
					new Object[] { encrypttext, ex.getMessage() }));
		}
		return null;
	}

	public static String decryptStringByJs(String encrypttext) {
		String text = decryptString(encrypttext);
		if (text == null) {
			return null;
		}
		return StringUtils.reverse(text);
	}

	public static RSAPublicKey getDefaultPublicKey() {
		KeyPair keyPair = getKeyPair();
		if (keyPair != null) {
			return (RSAPublicKey) keyPair.getPublic();
		}
		return null;
	}

	public static RSAPrivateKey getDefaultPrivateKey() {
		KeyPair keyPair = getKeyPair();
		if (keyPair != null) {
			return (RSAPrivateKey) keyPair.getPrivate();
		}
		return null;
	}

	public static String bigIntToHexStr(BigInteger bi) {
		return new String(Hex.encodeHex(bi.toByteArray()));
	}

	public static void main(String[] args) {
		try {
			String test = "aaa111";
			RSAPublicKey defalutPublicKey = getDefaultPublicKey();
			System.out.println(defalutPublicKey);
			System.out.println("m:" + new String(Hex.encodeHex(defalutPublicKey.getModulus() .toByteArray())));
			System.out.println("ex:" + new String(Hex.encodeHex(defalutPublicKey.getPublicExponent().toByteArray())));
			String en_test = encryptString(test);
			System.out.println("en=" + en_test);
			String de_test = decryptString(en_test);
			System.out.println(de_test);
			System.out.println(decryptString("04b93c02a85298e2fb2bb4cdf7fcf78b46c5a7395415256a79ae61009958999ce7deeb9ac42d41474ff63efd4484066efe41725e7d78274d8e8cc705dc56f54f593bd14b6b9e56705a2cd53c3ebfe7ec89f6c323c9924437d2832420b9e89092b299d876b153858c49a44f9c41db4348aea381fee640fbc5787aec852b8fb260"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static {
		try {
			Provider[] providers = Security.getProviders();
			for (int i = 0; i < providers.length; i++) {
				System.out.println(providers[i].toString());
			}
			keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException ex) {
			LOGGER.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
	}
}