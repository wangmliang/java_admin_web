package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.portal.common.entity.Menu;
import java.util.List;

/**
 * 菜单Mapper
 * <pre>
 * <b>Title：</b>MenuDao.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:33:39<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface MenuDao {
	/**
	 * 菜单列表
	 * @param paramMenu
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:33:45
	 */
	public abstract List<Menu> listMenu(Menu paramMenu);
}