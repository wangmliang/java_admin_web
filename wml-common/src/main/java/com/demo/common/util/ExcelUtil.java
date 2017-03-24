package com.demo.common.util;

import java.io.IOException;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 导出Excel工具类
 * <pre> 
 * <b>Title：</b>ExcelUtil.java<br/> 
 * <b>@author： </b>WML<br/> 
 * <b>@date：</b>2016年10月27日 下午1:52:00<br/> 
 * <b>Copyright (c) 2016 ASPire Tech.</b> 
 * </pre>
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 根据模板生成Excel文件.
     * @param templateSrcFilePath   模板文件存储路径.
     * @param beanParams            模板中存放的数据.
     * @param destFilePath          生成的文件.
     * @author WML 2016年10月27日 - 下午1:45:03
     */
    public static void createExcel(String templateSrcFilePath, Map<String, Object> beanParams, String destFilePath) {
        try {
            // 创建XLSTransformer对象
            XLSTransformer transformer = new XLSTransformer();
            
            // 生成Excel文件
            transformer.transformXLS(templateSrcFilePath, beanParams, destFilePath);
        } catch (ParsePropertyException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
