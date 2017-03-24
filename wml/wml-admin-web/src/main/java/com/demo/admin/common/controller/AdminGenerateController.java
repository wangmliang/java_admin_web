package com.demo.admin.common.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.webbas.core.exception.MyException;
import com.demo.admin.common.config.SystemConfig;
import com.demo.common.util.ExcelUtil;

/**
 * 代码自动生成Controller
 * <pre>
 * <b>Title：</b>AdminGenerateController.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月27日 下午4:34:18<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 *  </pre>
 */
@Controller
@RequestMapping("/generate")
public class AdminGenerateController extends BaseActionController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminGenerateController.class);
    
    /**
     * 代码自动生成
     * @param request
     * @param response
     * @author WML
     * 2016年10月27日 - 下午4:40:00
     */
    public void generate(HttpServletRequest request, HttpServletResponse response) {
        
    }
    
    /**
     * 导出excel文件
     * @param request
     * @param response
     * @author WML
     * 2016年10月27日 - 下午4:40:04
     */
    public void importExcel(HttpServletRequest request, HttpServletResponse response) {
        String excelUrl = SystemConfig.getInstance().getConfigPath();
        String tempUrl = SystemConfig.getInstance().getConfigPathTemp();
        
        if(StringUtils.isBlank(excelUrl) || StringUtils.isBlank(tempUrl)) {
            LOGGER.error("excel_url获取不到");
            return;
        }
        String templateSrcFilePath = excelUrl + "partnerProfitStatistics.xls";
        String destFilePath = tempUrl + "合作伙伴毛利统计.xls";
        Map<String, Object> beanParams = new HashMap<String, Object>();
        beanParams.put("list", null);
        
        ExcelUtil.createExcel(templateSrcFilePath, beanParams, destFilePath);
        try {
            this.downloadExcelFile(response, new File(destFilePath));
        } catch (IOException e) {
            LOGGER.error("导出报表出错！！！【"+ e.getMessage() +"】", e);
            throw new MyException("导出报表出错！！！【" + e.getMessage() + "】");
        }
    }
}
