package com.demo.admin.common.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspire.webbas.core.web.BaseController;

/**
 * 公用Controller
 * <pre>
 * <b>Title：</b>BaseActionController.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@date：</b>2016年10月31日 上午8:37:02<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 *  </pre>
 */
public class BaseActionController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseActionController.class);
    
    /**
     * excel导出
     * @param response
     * @param file
     * @throws IOException
     * @author WML
     * 2016年10月31日 - 上午8:37:35
     */
    protected void downloadExcelFile(HttpServletResponse response, File file) throws IOException {
        this.downloadFile(response, file, "application/vnd.ms-excel;charset=uft-8");
    }

    /**
     * 文件下载
     * @param response
     * @param file
     * @param contentType
     * @throws IOException
     * @author WML
     * 2016年10月31日 - 上午8:37:26
     */
    protected void downloadFile(HttpServletResponse response, File file, String contentType) throws IOException {
        String fileName = file.getName();
        response.reset();
        HttpServletRequest request = this.getRequest();
        response.setContentType(contentType);
        String agent = request.getHeader("USER-AGENT");
        if (null != agent && -1 != agent.indexOf("MSIE")) {// IE
            // 设置文件头，文件名称或编码格式
            response.addHeader("Content-Disposition",
                    "attachment;filename=\"" + java.net.URLEncoder.encode(fileName, "UTF-8") + "\"");
        } else {// firefox
            response.addHeader("Content-Disposition",
                    "attachment;filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
        }

        OutputStream myout = null;
        FileInputStream fis = null;
        try {
            // 读出文件到i/o流
            fis = new FileInputStream(file);
            BufferedInputStream buff = new BufferedInputStream(fis);
            byte[] b = new byte[1024];// 相当于我们的缓存
            long k = 0;// 该值用于计算当前实际下载了多少字节
            // 从response对象中得到输出流,准备下载
            myout = response.getOutputStream();
            // 开始循环下载
            while (k < file.length()) {
                int j = buff.read(b, 0, 1024);
                k += j;
                // 将b中的数据写到客户端的内存
                myout.write(b, 0, j);
            }
            myout.flush();
            if (buff != null) {
                buff.close();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(myout);
            IOUtils.closeQuietly(fis);
        }
    }
}
