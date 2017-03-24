package com.aspire.webbas.portal.common.config;

import com.aspire.webbas.configuration.config.ConfigurationHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户属性配置项
 * <pre>
 * <b>Title：</b>StaffExtendPropertyConfig.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:27:31<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public class StaffExtendPropertyConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(StaffExtendPropertyConfig.class);

	private static Configuration configuration = null;
	private static StaffExtendPropertyConfig config;
	private static final String DEFAULT_CONFIGURATION_FILENAME = "extend" + File.separator + "staff_extend_property.xml";

	private static String configurationFileName = DEFAULT_CONFIGURATION_FILENAME;

	private StaffExtendPropertyConfig() {
		if (configuration == null)
			refresh();
	}

	public static StaffExtendPropertyConfig getInstance() {
		if (config == null) {
			config = new StaffExtendPropertyConfig();
		}
		return config;
	}

	private static void refresh() {
		configuration = ConfigurationHelper.getConfiguration(configurationFileName, 50000L);
		if (configuration == null)
			LOGGER.error("读取配置文件失败, 配置文件：" + configurationFileName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<StaffField> getStaffFileds() {
		List list = new ArrayList();
		int size = configuration.getInt("fieldSize");
		for (int i = 0; i < size; i++) {
			StaffField staffField = new StaffField();
			staffField.setId(getString("fields.field(" + i + ").id"));

			staffField.setName(getString("fields.field(" + i + ").name"));

			staffField.setLabel(getString("fields.field(" + i + ").label"));

			staffField.setType(getString("fields.field(" + i + ").type"));

			staffField.setValue(getString("fields.field(" + i + ").value"));

			staffField.setCheck(getString("fields.field(" + i + ").check"));

			list.add(staffField);
		}
		return list;
	}

	public boolean isStaffExtendPropertyOn() {
		String support = getString("isStaffExtendPropertyOn", "false");
		return "true".equalsIgnoreCase(support);
	}

	private String getString(String arg) {
		if (configuration == null) {
			return null;
		}

		return configuration.getString(arg);
	}

	private String getString(String arg, String def) {
		if (configuration == null) {
			return def;
		}
		return configuration.getString(arg, def);
	}
}