package com.aspire.webbas.portal.modules.login.web;

import com.aspire.webbas.portal.common.util.CheckCodeUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 验证码Controller
 * <pre>
 * <b>Title：</b>CodeController.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月7日 - 下午5:53:48<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
@Controller
@RequestMapping({ "/code" })
public class CodeController {
	
	/**
	 * 获取验证码
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @author WML
	 * 2016年11月7日 - 下午5:54:06
	 */
	@RequestMapping({ "/getCode.ajax" })
	public void getCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		BufferedImage image = CheckCodeUtil.createImage(session);
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0L);
		resp.setContentType("image/jpeg");
		ServletOutputStream sos = resp.getOutputStream();
		ImageIO.write(image, "jpeg", sos);
		sos.close();
	}
}