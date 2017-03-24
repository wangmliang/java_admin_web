package com.aspire.webbas.portal.modules.authmgt.web;

import com.aspire.webbas.core.util.StringTools;
import com.aspire.webbas.core.web.BaseController;
import com.aspire.webbas.portal.common.entity.PendTaskSetting;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.service.PendTaskSettingService;
import com.aspire.webbas.portal.common.util.StaffUtil;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ��־��¼Controller
 * <pre>
 * <b>Title��</b>PendTaskSettingController.java<br/>
 * <b>@author��</b>WML<br/>
 * <b>@date��</b>2016��11��8�� - ����8:44:24<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
@Controller
@RequestMapping({ "/pendTaskSetting" })
public class PendTaskSettingController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(PendTaskSettingController.class);

	@Autowired
	@Qualifier("pendTaskSettingService")
	private PendTaskSettingService pendTaskSettingService;

	/**
	 * ���¼�¼
	 * @param data
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����8:45:25
	 */
	@RequestMapping({ "/update.ajax" })
	@ResponseBody
	public Map<String, Object> update(@ModelAttribute("pendTaskSetting") PendTaskSetting data) {
		try {
			this.pendTaskSettingService.saveAndUpdate(data);
		} catch (Exception e) {
			logger.error("���¼�¼����", e);
			return super.fail("���¼�¼����ϵͳ����");
		}
		return super.success("���³ɹ�");
	}

	/**
	 * ��ȡ��¼
	 * @return
	 * @author WML
	 * 2016��11��8�� - ����8:45:50
	 */
	@RequestMapping({ "/get.ajax" })
	@ResponseBody
	public Map<String, Object> get() {
		try {
			Staff staff = StaffUtil.getLoginStaff();
			PendTaskSetting data = this.pendTaskSettingService.get(staff.getStaffId().toString());

			Map<String, Object> result = new HashMap<String, Object>();
			if (null != data) {
				if (isEmpty(data.getSendemail())) {
					data.setSendemail("0");
				}
				if (isEmpty(data.getSendsms())) {
					data.setSendsms("0");
				}
			}
			if (null != staff) {
				staff.setEmail(StringTools.null2Str(staff.getEmail()));
				staff.setMobile(StringTools.null2Str(staff.getMobile()));
			}
			result.put("staff", staff);
			result.put("pendTaskSetting", data);
			return super.success(result);
		} catch (Exception e) {
			logger.error("��ȡ��¼����", e);
		}
		return super.fail("��ȡ��¼����ϵͳ����");
	}
}