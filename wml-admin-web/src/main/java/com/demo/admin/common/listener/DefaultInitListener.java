package com.demo.admin.common.listener;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspire.webbas.configuration.config.ConfigurationHelper;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * 初始化监听器
 * <pre>
 * <b>Title：</b>DefaultInitListener.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月31日 上午9:33:26<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class DefaultInitListener implements ServletContextListener {
    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultInitListener.class);
    
    /**
     * application root key
     */
    private static final String APP_ROOT_KEY = "APP_ROOT";
    
    /**
     * config root key
     */
    private static final String CONFIG_ROOT = "configPath";
    
    /**
     * application root
     */
    private String appRootPath;

    private Scheduler scheduler = null;

    /**
     * 
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LOGGER.info("Web Application Context【" + appRootPath + "】 Destroyed.");
        try {
            if (null != scheduler) {
                scheduler.shutdown(true);
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            LOGGER.error("ShutdownQuartz.contextDestroyed", e);
        }
    }

    /**
     * 
     */
    public void contextInitialized(ServletContextEvent event) {
        ServletContext sc = event.getServletContext();
        LOGGER.info("Initializing Web Application Context【" + sc.getRealPath("/") + "】.");
        appRootPath = sc.getRealPath("/");
        System.setProperty(APP_ROOT_KEY, appRootPath);
        if (System.getProperty(APP_ROOT_KEY) == null) {
            System.setProperty(APP_ROOT_KEY, sc.getRealPath("/"));
            LOGGER.info("Initializing Web Application Context[" + System.getProperty(APP_ROOT_KEY) + "].");
        }
        /**
         *  初始化配置文件根目录
         */
        initRootConfigPath();

        /**
         *  初始化log4j配置(日志输出目录等)
         */
        initLogback();
        
        /**
         * 初始化任务调度
         */
        initQuartz();
    }

    /**
     * 任务调度
     * @author WML
     * 2016年10月31日 - 上午9:34:31
     */
    private void initQuartz() {
        StdSchedulerFactory factory;
        FileInputStream fin = null;
        try {
            // 获取配置文件路径
            String configFile = ConfigurationHelper.getBasePath() + File.separator + "quartz.properties";
            if (configFile != null) {
                fin = new FileInputStream(configFile);
                Properties configProperties = new Properties();
                configProperties.load(fin);
                String[] quartzJobFileNames = configProperties.getProperty("org.quartz.plugin.jobInitializer.fileName").split(",");
                StringBuilder buf = new StringBuilder();
                for (String fileName : quartzJobFileNames) {
                    String filePath = ConfigurationHelper.getBasePath() + File.separator + fileName;
                    buf.append("," + filePath);
                }
                String quartzJobConfig = buf.substring(1);

                configProperties.setProperty("org.quartz.plugin.jobInitializer.fileName", quartzJobConfig);
                factory = new StdSchedulerFactory(configProperties);
                scheduler = factory.getScheduler();
                scheduler.start();
            }
        } catch (Exception e) {
            LOGGER.error("初始化定时任务异常： ", e);
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception ex) {
                    LOGGER.error("关闭文件流出错", ex);
                    fin = null;
                }
            }
        }
    }

    /**
     * 清理资源（相关timer的关闭）.
     */
    public void destroy() {
    }

    /**
     * 初始化配置文件根目录.
     */
    private void initRootConfigPath() {

        System.out.print("设置系统配置文件根目录....");
        // 初始化配置文件根目录
        /**********************************
         * 常用服务器缺省存放配置文件根目录： TOMCAT：$TOMCAT_HOME/bin/config OC4J:
         * $OC4J_HOME/j2ee/home/sims/config
         **********************************/
        String userDir = getUserDir();
        String configRootPath = userDir + "/config";
        ConfigurationHelper.setBasePath(configRootPath);

        System.out.println("done! 配置文件根目录:" + configRootPath);
    }

    /**
     * 初始化日志打印记录
     * @author WML
     * 2016年10月31日 - 上午9:34:43
     */
    private void initLogback() {
        System.out.print("加载logback.xml配置文件....");
        String logRootPath = ConfigurationHelper.getBasePath() + "/logback/logback.xml";
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        try {
            configurator.doConfigure(logRootPath);
        } catch (JoranException e) {
            System.out.println("加载" + logRootPath + "出错!error=" + e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("done!");
    }

    /**
     * 得到user.dir
     * 
     * @return user.dir
     */
    private String getUserDir() {
        // 首先从系统属性中读取
        String configPath = System.getProperty(CONFIG_ROOT);
        if (!StringUtils.isEmpty(configPath)) {
            return configPath;
        }
        return System.getProperty("user.dir");
    }

}
