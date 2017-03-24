package com.aspire.webbas.portal.common.listener;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.aspire.webbas.configuration.config.ConfigurationHelper;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class DefaultInitListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(DefaultInitListener.class);
	private static final String APP_ROOT_KEY = "APP_ROOT";

	private static final String LOG_ROOT = "logbase.dir";

	private static final String CONFIG_ROOT = "configPath";
	private String appRootPath;

	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		logger.info("Initializing Web Application Context【" + sc.getRealPath("/") + "】.");
		System.setProperty("APP_ROOT", this.appRootPath = sc.getRealPath("/"));
		if (System.getProperty("APP_ROOT") == null) {
			System.setProperty("APP_ROOT", sc.getRealPath("/"));
			logger.info("Initializing Web Application Context[" + System.getProperty("APP_ROOT") + "].");
		}
		initRootConfigPath(sc);
		intiLogBack(sc);
	}

	private void initRootConfigPath(ServletContext sc) {
		logger.info("设置系统配置文件根目录....");

		String configPath = System.getProperty("configPath");
		if (StringUtils.isEmpty(configPath)) {
			configPath = getUserDir() + "/config";
		}
		System.setProperty("configPath", configPath);
		ConfigurationHelper.setBasePath(configPath);

		logger.info("done! 配置文件根目录:" + configPath);
	}

	private String getUserDir() {
		return System.getProperty("user.dir");
	}

	private void intiLogBack(ServletContext sc) {
		String logRoot = System.getProperty("logbase.dir");

		if (StringUtils.isEmpty(logRoot)) {
			logRoot = sc.getInitParameter("logbase.dir");
		}

		if (StringUtils.isEmpty(logRoot)) {
			logRoot = getUserDir();
		}
		System.setProperty("logbase.dir", logRoot);
		logger.info( "Set the system Log file root directory is successful, the Log directory is:【{}】", logRoot);

		String logbackFile = "";
		try {
			logbackFile = ConfigurationHelper.getBasePath() + "/logback/logback.xml";
			if ((logbackFile != null) && (!logbackFile.isEmpty())) {
				LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
				lc.reset();
				JoranConfigurator configurator = new JoranConfigurator();
				configurator.setContext(lc);
				configurator.doConfigure(logbackFile);
				logger.info("Loaded logback configure file from {}.", logbackFile);
			} else {
				logger.info("No logback configuration location specified, keepping default.");
			}
		} catch (Exception e) {
			logger.error("Fail to loading logback configuration from " + logbackFile + ", keepping default.", e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		logger.info("Web Application Context【" + this.appRootPath + "】 Destroyed.");
	}
}