package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.portal.common.entity.Resource;
import java.util.List;
import java.util.Map;

/**
 * 资源Mapper
 * <pre>
 * <b>Title：</b>ResourceDao.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月9日 - 上午8:43:09<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface ResourceDao {
	
	/**
	 * 添加
	 * @param paramResource
	 * @author WML
	 * 2016年11月9日 - 上午8:43:57
	 */
	public abstract void insertResource(Resource paramResource);

	/**
	 * 根据MetadataId删除数据
	 * @param paramString
	 * @author WML
	 * 2016年11月9日 - 上午8:44:07
	 */
	public abstract void deleteResourceByMetadataId(String paramString);

	/**
	 * 更新
	 * @param paramResource
	 * @author WML
	 * 2016年11月9日 - 上午8:44:33
	 */
	public abstract void updateResource(Resource paramResource);

	/**
	 * 根据id查询数据
	 * @param paramLong	RESOURCE_ID
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:44:37
	 */
	public abstract Resource findResource(Long paramLong);

	/**
	 * 根据key查询数据
	 * @param paramString	RESOURCE_KEY
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:44:48
	 */
	public abstract Resource findResourceByKey(String paramString);

	/**
	 * 根据resource查询数据列表
	 * @param paramResource	Resource
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:44:58
	 */
	public abstract List<Resource> listResource(Resource paramResource);

	/**
	 * 根据loginName查询资源列表
	 * @param paramString	LOGIN_NAME	
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:45:25
	 */
	public abstract List<Resource> listResourceByLoginName(String paramString);

	/**
	 * 根据roleIdc查询资源列表
	 * @param paramLong		ROLE_ID
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:45:49
	 */
	public abstract List<Resource> listResourceByRoleId(Long paramLong);

	/**
	 * 根据资源和操作查询所有的资源信息
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:46:10
	 */
	public abstract List<Map<String, String>> listAllResourceAndOperation();

	/**
	 * 查询登录用户的所有资源
	 * @param paramLong		STAFF_ID
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:46:44
	 */
	public abstract List<Map<String, String>> listResourceOfStaff(Long paramLong);
}