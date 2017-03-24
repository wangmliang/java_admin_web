package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.portal.common.entity.PendTaskSetting;
import java.util.List;

/**
 * 
 * <pre>
 * <b>Title：</b>PendTaskSettingMapper.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:53:10<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface PendTaskSettingMapper {
	
	public abstract int deleteByPrimaryKey(String paramString);

	public abstract int insert(PendTaskSetting paramPendTaskSetting);

	public abstract int insertSelective(PendTaskSetting paramPendTaskSetting);

	public abstract List<PendTaskSetting> pageQuery(Page<PendTaskSetting> paramPage);

	public abstract PendTaskSetting selectByPrimaryKey(String paramString);

	public abstract int updateByPrimaryKeySelective(PendTaskSetting paramPendTaskSetting);

	public abstract int updateByPrimaryKey(PendTaskSetting paramPendTaskSetting);
}