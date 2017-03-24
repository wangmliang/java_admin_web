package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.portal.common.entity.Operation;
import java.util.List;

/**
 * 操作资源Mapper
 * <pre>
 * <b>Title：</b>OperationDao.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:49:20<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface OperationDao {
	/**
	 * 添加操作资源
	 * @param paramOperation
	 * @author WML
	 * 2016年11月8日 - 下午5:49:36
	 */
	public abstract void insertOperation(Operation paramOperation);

	/**
	 * 删除操作资源
	 * @param paramOperation
	 * @author WML
	 * 2016年11月8日 - 下午5:49:45
	 */
	public abstract void deleteOperation(Operation paramOperation);

	/**
	 * 更新操作资源
	 * @param paramOperation
	 * @author WML
	 * 2016年11月8日 - 下午5:49:54
	 */
	public abstract void updateOperation(Operation paramOperation);

	/**
	 * 查询操作资源信息
	 * @param paramOperation
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:50:07
	 */
	public abstract Operation findOperation(Operation paramOperation);

	/**
	 * 根据Operation对象查询列表
	 * @param paramOperation
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:50:43
	 */
	public abstract List<Operation> listOperation(Operation paramOperation);

	/**
	 * 根据用户id查询资源列表信息
	 * @param paramLong
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:50:56
	 */
	public abstract List<Operation> listResourceOperationByStaffId(Long paramLong);

	/**
	 * 根据操作key和用户id查询资源key
	 * @param paramLong
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:51:11
	 */
	public abstract List<Operation> listResourceKeyAndOperationKeyStaffId(Long paramLong);

	/**
	 * 根据角色id查询操作列表
	 * @param paramLong
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:52:07
	 */
	public abstract List<Operation> listOperationByRoleId(Long paramLong);
}