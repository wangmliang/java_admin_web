package com.aspire.webbas.portal.common.util;

import com.aspire.webbas.portal.common.config.Config;
import com.aspire.webbas.portal.common.entity.Staff;

public class PasswordAdapter {
	private Staff staff;

	public PasswordAdapter(Staff s) {
		this.staff = s;
	}

	public String encryptPassword() {
		if (Config.getInstance().isOldPasswordSupport()) {
			return PasswordUtil.buildPassword(this.staff.getStaffId()
					.toString(), this.staff.getPassword());
		}

		return PasswordUtil.buildPassword(this.staff.getLoginName(),
				this.staff.getPassword());
	}
}