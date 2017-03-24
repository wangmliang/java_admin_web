package com.demo.admin.common.job;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.admin.common.config.SystemConfig;

/**
 * 测试job
 * <pre>
 * <b>Title：</b>TestJob.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月27日 下午4:36:04<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 *  </pre>
 */
public class TestJob implements StatefulJob {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("This is Job..." + new Date().getTime() + "——" + SystemConfig.getInstance().getTest());
        LOGGER.info("This is Job...[info]" + new Date().getTime() + "——" + SystemConfig.getInstance().getTest());
        LOGGER.debug("This is Job...[debug]" + new Date().getTime() + "——" + SystemConfig.getInstance().getTest());
        LOGGER.error("This is Job...[error]" + new Date().getTime() + "——" + SystemConfig.getInstance().getTest());
    }
}
