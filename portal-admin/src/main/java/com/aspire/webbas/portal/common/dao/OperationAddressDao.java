package com.aspire.webbas.portal.common.dao;

import com.aspire.webbas.portal.common.entity.OperationAddress;
import java.util.List;
import java.util.Map;

/**
 * 操作路径地址
 * <pre>
 * <b>Title：</b>OperationAddressDao.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2016年11月8日 - 下午5:43:59<br/>  
 * <b>@version v1.0</b></br/>
 * <b>Copyright (c) 2016 ASPire Tech.</b>   
 * </pre>
 */
public abstract interface OperationAddressDao {
	
	/**
	 * 添加操作地址
	 * @param paramOperationAddress
	 * @author WML
	 * 2016年11月8日 - 下午5:45:25
	 */
	public abstract void insertOperationAddress(OperationAddress paramOperationAddress);

	/**
	 * 删除操作地址
	 * @param paramOperationAddress
	 * @author WML
	 * 2016年11月8日 - 下午5:45:54
	 */
	public abstract void deleteOperationAddress(OperationAddress paramOperationAddress);

	/**
	 * 根据addressUrl查询操作地址
	 * @param paramString
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:46:04
	 */
	public abstract OperationAddress findOperationAddress(String paramString);

	/**
	 * 操作地址列表
	 * @param paramOperationAddress
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:47:00
	 */
	public abstract List<OperationAddress> listOperationAddress(OperationAddress paramOperationAddress);

	/**
	 * 根据登录名查询操作地址列表
	 * @param paramString
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:47:21
	 */
	public abstract List<OperationAddress> listOperationAddressByLoginName(String paramString);

	/**
	 * 根据staff_id+resource_id+operation_key操作地址count数
	 * @param paramMap
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:47:43
	 */
	public abstract Integer findOperationAddressByStaff(Map<String, Object> paramMap);

	/**
	 * 根据AUTH_TYPE查询操作地址列表
	 * @param paramString
	 * @return
	 * @author WML
	 * 2016年11月8日 - 下午5:47:46
	 */
	public abstract List<OperationAddress> listOperationAddressByAuthType(String paramString);
}