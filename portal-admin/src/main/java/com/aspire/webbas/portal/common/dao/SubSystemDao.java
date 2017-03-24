package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.portal.common.entity.SubSystem;
import java.util.List;

/**
 * 子系统Mapper
 * <pre>
 * <b>Title：</b>SubSystemDao.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月9日 - 上午9:07:49<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface SubSystemDao {
	/**
	 * 子系统列表
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午9:07:56
	 */
	public abstract List<SubSystem> listSubSystem();
}