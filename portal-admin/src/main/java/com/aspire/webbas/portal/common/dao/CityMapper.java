package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.portal.common.entity.City;
import java.util.List;

/**
 * 城市Mapper
 * <pre>
 * <b>Title：</b>CityMapper.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:28:33<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface CityMapper {
	
	/**
	 * 删除
	 * @param paramInteger
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:28:42
	 */
	public abstract int deleteByPrimaryKey(Integer paramInteger);

	/**
	 * 添加
	 * @param paramCity
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:28:46
	 */
	public abstract int insert(City paramCity);

	/**
	 * 添加(过去空属性)
	 * @param paramCity
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:28:51
	 */
	public abstract int insertSelective(City paramCity);

	/**
	 * 城市列表
	 * @param paramCity
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:29:08
	 */
	public abstract List<City> list(City paramCity);

	/**
	 * 根据id查询对应城市
	 * @param paramInteger
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:29:18
	 */
	public abstract City selectByPrimaryKey(Integer paramInteger);

	/**
	 * 更新(过滤空属性)
	 * @param paramCity
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:29:38
	 */
	public abstract int updateByPrimaryKeySelective(City paramCity);

	/**
	 * 更新
	 * @param paramCity
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:29:52
	 */
	public abstract int updateByPrimaryKey(City paramCity);
}