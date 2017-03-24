package com.demo.admin.common.controller;

import java.sql.Connection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aspire.webbas.core.web.BaseController;

/**
 * 代码生成Controller
 * <pre>
 * <b>Title：</b>GeneratedController.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月18日 下午3:37:55<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 *  </pre>
 */
@Controller
@RequestMapping("/generated")
public class GeneratedController extends BaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(GeneratedController.class);
    
    /**
     * 自动生成代码入口
     * @param generateInfo 待自动生成对象  
     * @param dbUtils      数据库连接对象
     * @param request   
     * @param response
     * @return
     * @author WML
     * 2016年10月18日-下午3:39:18
     */
    /*@RequestMapping(value = "/generatedCode")
    @ResponseBody
    public Map<String, Object> generatedCode(GenerateInfo generateInfo, DBUtils dbUtils, HttpServletRequest request, HttpServletResponse response) {
        if(null == generateInfo || null == dbUtils) {
            logger.error("异常：[GenerateInfo] 或 [DBUtils]对象为空");
            return super.fail("参数异常");
        }
        
        *//**
         * 设置 模板读取路径
         *//*
        generateInfo.setFtlPath(FreemarkerUtil.class.getClassLoader().getResource("").getFile() + "ftl/createCode/");
        String fileDir = "/static/upload/com/";
        generateInfo.setEntityFilePath(fileDir + generateInfo.getEntityPackage().replaceAll("\\.", "/") + "/");
        generateInfo.setMapperFilePath(fileDir + generateInfo.getMapperPackage().replaceAll("\\.", "/") + "/");
        generateInfo.setXmlFilePath(fileDir + generateInfo.getXmlPackage().replaceAll("\\.", "/") + "/");
        
        *//**
         *  截取数据库名称
         *//*
        String dbInfo = "";
        String[] urlStr = dbUtils.getUrl().split("/");
        if(urlStr.length == 1) {
            urlStr = dbUtils.getUrl().split("\\:");
        } 
        dbInfo = urlStr[urlStr.length - 1];
        
        // 生成代码入口
        Connection connection = JdbcConnectionUtil.getDatabaseMetaData(dbUtils.getDriver(), dbUtils.getUrl(), dbUtils.getUser(), dbUtils.getPassword());
        GeneratedApplication.generate(generateInfo, connection, dbInfo);
        return super.success("自动生成成功");
    }*/
    
    /**
     * 导出Zip
     * @param request
     * @param response
     * @author WML
     * 2016年10月18日-下午5:00:28
     */
    /*@RequestMapping(value = "/download.ajax")
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response) {
        try {
            String commUrl = FreemarkerUtil.class.getClassLoader().getResource("../../").getFile();
            String url = commUrl + "static/upload/com";
            
            String outUrl = url + new Date().getTime() + ".zip";
            ZipUtils.createZip(url, outUrl);
            File f = new File(outUrl); 
            if (!f.exists()) { 
                response.sendError(404, "File not found!"); 
                return; 
            } 
            BufferedInputStream br = new BufferedInputStream(new FileInputStream(f)); 
            byte[] buf = new byte[1024]; 
            int len = 0; 
            response.reset(); 
            response.setContentType("application/x-msdownload"); 
            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName()); 
            OutputStream out = response.getOutputStream(); 
            while ((len = br.read(buf)) > 0) 
                out.write(buf, 0, len); 
            br.close();
            out.close();
            
            //删除文件
            PathUtil.delete(url);
            PathUtil.delete(outUrl);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Zip文件下载异常：" + e.getMessage(), e);
        }
    }*/
    
}
