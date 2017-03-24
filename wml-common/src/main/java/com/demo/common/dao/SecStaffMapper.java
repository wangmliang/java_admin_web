/*
 * SecStaffMapper.java
 * 因纳特科技有限公司
 * All rights reserved.
 * -----------------------------------------------
 * 2016-10-31 15:37:54  Created
 */
package com.demo.common.dao;

import java.util.List;
import java.util.Map;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.demo.common.entity.SecStaff;

/**
 * 成员Mapper
 * <pre>
 * <b>Title：</b>SecStaffMapper.java<br/>
 * <b>@author： </b>WML<br/>
 * <b>@version：</b>@version V1.0<br/>
 * <b>@date：</b>2016-10-31 15:37:54 Created<br/>  
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public interface SecStaffMapper {

	/**
	 * 数据列表
	 * @param  map	
	 * @return 数据列表
	 * @author WML
	 * 2016-10-31 15:37:54  Created
	 */
	List<SecStaff> getPageList(Map<String, Object> map);

	/**
     * 根据主键id删除对应数据
     * @param  record	主键id
     * @return 删除影响行数
     * @author WML
     * 2016-10-31 15:37:54  Created
     */
	int deleteByPrimaryKey(Long id);

	/**
     * 新增
     * @param record	SecStaff对象
     * @return 新增影响行数
     * @author WML
     * 2016-10-31 15:37:54  Created
     */
    int insert(SecStaff record);

	/**
     * 新增（属性通过非空验证，为空属性不参与操作）
     * @param  record	SecStaff对象
     * @return 新增影响行数
     * @author WML
     * 2016-10-31 15:37:54  Created
     */
    int insertSelective(SecStaff record);

	/**
     * 根据主键id查询对应数据
     * @param  record	主键id
     * @return SecStaff对象
     * @author WML
     * 2016-10-31 15:37:54  Created
     */
    SecStaff selectByPrimaryKey(Long id);

	/**
     * 查询SecStaff对象的数据集合
     * @return SecStaff对象集合
     * @author WML
     * 2016-10-31 15:37:54  Created
     */
	List<SecStaff> selectAll();
	
	/**
     * 根据SecStaff对象更新对应数据
     * @param  record	SecStaff对象
     * @return 更新影响行数
     * @author WML
     * 2016-10-31 15:37:54  Created
     */
    int updateByPrimaryKeySelective(SecStaff record);

	/**
     * 根据SecStaff对象更新对应数据（属性通过非空验证，为空属性不参与操作）
     * @param  record	SecStaff对象
     * @return 更新影响行数
     * @author WML
     * 2016-10-31 15:37:54  Created
     */
    int updateByPrimaryKey(SecStaff record);
    
    /**
     * 根据Map对象批量删除
     * @param  map	map对象  [ list:主键id集合 ]
     * @return 批量删除影响行数
     * @author WML
     * 2016-10-31 15:37:54  Created
     */
    int batchDelete(Map<String, Object> map);
    
    /**
     * 数据列表
     * @param page  参数对象
     * @return
     * @author WML
     * 2016年10月31日 - 下午3:40:18
     */
    List<SecStaff> pageQuery(Page<SecStaff> page);
}