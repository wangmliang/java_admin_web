package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.portal.common.entity.ResourceCategory;
import java.util.List;

/**
 * 资源类别Mapper
 * <pre>
 * <b>Title：</b>ResourceCategoryDao.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月9日 - 上午8:37:43<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface ResourceCategoryDao {
	
	/**
	 * 添加
	 * @param paramResourceCategory
	 * @author WML
	 * 2016年11月9日 - 上午8:37:55
	 */
	public abstract void insertResourceCategory(ResourceCategory paramResourceCategory);

	/**
	 * 根据id删除
	 * @param paramLong
	 * @author WML
	 * 2016年11月9日 - 上午8:38:01
	 */
	public abstract void deleteResourceCategoryById(Long paramLong);

	/**
	 * 根据MetadataId删除
	 * @param paramString
	 * @author WML
	 * 2016年11月9日 - 上午8:38:09
	 */
	public abstract void deleteResourceCategoryByMetadataId(String paramString);

	/**
	 * 更新
	 * @param paramResourceCategory
	 * @author WML
	 * 2016年11月9日 - 上午8:38:25
	 */
	public abstract void updateResourceCategory(ResourceCategory paramResourceCategory);

	/**
	 * 根据id查询数据
	 * @param paramLong
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:39:06
	 */
	public abstract ResourceCategory findResourceCategory(Long paramLong);

	/**
	 * 根据key查询数据
	 * @param paramString
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:39:18
	 */
	public abstract ResourceCategory findResourceCategoryByKey(String paramString);

	/**
	 * 根据ResourceCategory查询数据列表
	 * @param paramResourceCategory
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:39:42
	 */
	public abstract List<ResourceCategory> listResourceCategory(ResourceCategory paramResourceCategory);

	/**
	 * 根据CATEGORY_KEY not like查询数据列表
	 * @param paramResourceCategory
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:39:42
	 */
	public abstract List<ResourceCategory> listRootResourceCategory(ResourceCategory paramResourceCategory);

	/**
	 * 根据父级id查询数据
	 * @param paramLong
	 * @return
	 * @author WML
	 * 2016年11月9日 - 上午8:42:07
	 */
	public abstract List<ResourceCategory> listResourceCategoryByParentId(Long paramLong);
}