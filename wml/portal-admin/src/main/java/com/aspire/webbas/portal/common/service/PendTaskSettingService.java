package com.aspire.webbas.portal.common.service;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.portal.common.dao.PendTaskSettingMapper;
import com.aspire.webbas.portal.common.entity.PendTaskSetting;
import com.aspire.webbas.portal.common.entity.Staff;
import com.aspire.webbas.portal.common.util.StaffUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pendTaskSettingService")
public class PendTaskSettingService {

	@Autowired
	private PendTaskSettingMapper pendTaskSettingMapper;

	public PendTaskSetting get(String staffid) {
		return this.pendTaskSettingMapper.selectByPrimaryKey(staffid);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void insert(PendTaskSetting data) {
		this.pendTaskSettingMapper.insert(data);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void saveAndUpdate(PendTaskSetting data) throws Exception {
		Staff staff = StaffUtil.getLoginStaff();
		data.setStaffid(staff.getStaffId().toString());
		PendTaskSetting pendTaskSetting = get(staff.getStaffId().toString());
		if (null != pendTaskSetting)
			update(data);
		else
			insert(data);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page<PendTaskSetting> pageQuery(Page<PendTaskSetting> page) {
		List list = this.pendTaskSettingMapper.pageQuery(page);
		page.setDatas(list);
		return page;
	}

	@Transactional(rollbackFor = { Exception.class })
	public void update(PendTaskSetting data) {
		this.pendTaskSettingMapper.updateByPrimaryKey(data);
	}

	@Transactional(rollbackFor = { Exception.class })
	public int delete(String staffid) {
		return this.pendTaskSettingMapper.deleteByPrimaryKey(staffid);
	}
}