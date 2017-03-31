package com.wangml.utils;

import java.io.IOException;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * excel导入导出工具类
 * 
 * <pre>
 * <b>Title：</b>ExcelUtil.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月31日 - 下午2:24:50<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>
 * </pre>
 */
public class ExcelUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * 根据模板生成Excel文件.
	 * 
	 * @param templateFileName
	 *            模板文件.
	 * @param list
	 *            模板中存放的数据.
	 * @param resultFileName
	 *            生成的文件.
	 */
	public static void createExcel(String templateSrcFilePath, Map<String, Object> beanParams, String destFilePath) {
		// 创建XLSTransformer对象
		XLSTransformer transformer = new XLSTransformer();
		try {
			// 生成Excel文件
			transformer.transformXLS(templateSrcFilePath, beanParams, destFilePath);
		} catch (ParsePropertyException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
