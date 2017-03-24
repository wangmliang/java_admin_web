package com.aspire.webbas.portal.common.config;

import com.aspire.webbas.configuration.config.ConfigurationHelper;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置信息
 * <pre>
 * <b>Title：</b>Config.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:23:03<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class Config {
	private static final Logger logger = LoggerFactory.getLogger(Config.class);

	/**
	 * 配置对象
	 */
	private static Configuration configuration = null;
	
	private static Config config;
	
	/**
	 * 默认配置文件名称
	 */
	@SuppressWarnings("unused")
	private static final String DEFAULT_CONFIGURATION_FILENAME = "webbas.xml";
	
	/**
	 * 配置文件名称
	 */
	private static String configurationFileName = "webbas.xml";

	private Config() {
		if (configuration == null)
			refresh();
	}

	public static Config getInstance() {
		if (config == null) {
			config = new Config();
		}
		return config;
	}

	private static void refresh() {
		configuration = ConfigurationHelper.getConfiguration(configurationFileName, 50000L);
		if (configuration == null)
			logger.error("读portal配置文件失败, 配置文件：" + configurationFileName);
	}

	public String getTitle() {
		return getString("portal.title");
	}

	public boolean isOldPasswordSupport() {
		String support = getString("portal.old-password-crypt-support", "off");

		return "on".equalsIgnoreCase(support);
	}

	public boolean isCheckCodeOn() {
		String support = getString("portal.checkCode", "false");
		return "true".equalsIgnoreCase(support);
	}

	public boolean isContractAgreementOn() {
		String support = getString("portal.contractAgreement", "false");
		return "true".equalsIgnoreCase(support);
	}

	public boolean isRegisterOn() {
		String support = getString("portal.is-register-on", "false");
		return "true".equalsIgnoreCase(support);
	}

	public String getRegisterUrl() {
		return getString("portal.register-url");
	}

	public boolean isForgotpwdOn() {
		String support = getString("portal.is-forgotpwd-on", "false");
		return "true".equalsIgnoreCase(support);
	}

	public String getForgotpwdUrl() {
		return getString("portal.forgotpwd-url");
	}

	private String getString(String arg) {
		if (configuration == null) {
			return null;
		}

		return configuration.getString(arg);
	}

	private String getString(String arg, String def) {
		if (configuration == null) {
			return def;
		}
		return configuration.getString(arg, def);
	}
}