package com.demo.common.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 缓存管理操作类 K,键类型，V 值类型
 * <pre>
 * <b>Title：</b>ICacheManager.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月2日 - 下午4:30:44<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public interface ICacheManager {

	/**
	 * 从缓存中取出结果
	 * @param key
	 * @return
	 * @author WML
	 * 2017年3月3日 - 上午9:04:27
	 */
	public String get(String key);

	/**
	 * 设置一个内容放到缓存中
	 * @param key
	 * @param value
	 * @return
	 * @author WML
	 * 2017年3月3日 - 上午9:04:22
	 */
	public boolean set(String key, String value);

	/**
	 * 从缓存中删除内容
	 * @param key
	 * @author WML
	 * 2017年3月3日 - 上午9:04:18
	 */
	public void remove(String key);

	/**
	 * 缓存值，并设置过期时间
	 * @param key
	 * @param value
	 * @param timeout
	 * @return
	 * @author WML
	 * 2017年3月3日 - 上午9:00:51
	 */
	public boolean set(String key, String value, int timeout);

	/**
	 * 得到cache元素个数
	 * @return 元素个数
	 * @author WML
	 * 2017年3月3日 - 上午9:03:54
	 */
	public int size();

	/**
	 * 得到多条记录
	 * @param keys
	 * @return 元素集合
	 * @author WML
	 * 2017年3月3日 - 上午9:04:07
	 */
	public Map<String, String> getMulti(Collection<String> keys);

	/**
	 * 清空缓存
	 * @author WML
	 * 2017年3月3日 - 上午9:03:49
	 */
	public void clear();

	/**
	 * 自增长 如果key不存在，则将默认值设为0，再执行加incrBy操作
	 * @param key
	 * @param incrBy	增长幅度
	 * @return
	 * @author WML
	 * 2017年3月3日 - 上午9:03:40
	 */
	public long incr(String key, int incrBy);

	/**
	 * 自增的同时会设置过期时间，piple管道操作
	 * @param key
	 * @param incrBy
	 * @param timeOut
	 * @return
	 * @author WML
	 * 2017年3月3日 - 上午9:03:35
	 */
	public long incrExpir(String key, int incrBy, int timeOut);

	/**
	 * 管道批量更新增长 ,List<Object>[0]:key,List<Object>[1]:incrBy,List<Object>[1]:timeOut
	 * @param incrs
	 * @author WML
	 * 2017年3月3日 - 上午9:03:16
	 */
	public void mutiIncrExpir(List<List<Object>> incrs);

	/**
	 * 自减，如果key不存在，则将默认值设为0，再执行减去descrBy操作
	 * @param key
	 * @param decrBy
	 * @return
	 * @author WML
	 * 2017年3月3日 - 上午9:03:10
	 */
	public long decr(String key, int decrBy);

	/**
	 * 设置过期时间:从设置的时间开始，多长时间后过期
	 * @param key
	 * @param timeout
	 * @author WML
	 * 2017年3月3日 - 上午9:03:05
	 */
	public void expirTime(String key, int timeout);

	/**
	 * UTC(GMT)时间都是从（1970年01月01日 0:00:00)开始计算秒数的
	 * @param key
	 * @param value
	 * @param timeout
	 * @author WML
	 * 2017年3月3日 - 上午9:02:31
	 */
	public void expirAfter1970(String key, String value, long timeout);

	/**
	 * map批量自增
	 * @param key
	 * @param kvMap
	 * @param expire
	 * @author WML
	 * 2017年3月3日 - 上午9:02:26
	 */
	public void hincr(String key, Map<String, Integer> kvMap, long expire);

	/**
	 * 获取map的所有数据
	 * @param key
	 * @return
	 * @author WML
	 * 2017年3月3日 - 上午9:02:21
	 */
	public Map<String, String> hgetAll(String key);

	/**
	 * 对某个字段进行自增
	 * @param key
	 * @param fieldKey
	 * @param incrBy
	 * @return
	 * @author WML
	 * 2017年3月3日 - 上午9:02:16
	 */
	public long hincr(String key, String fieldKey, long incrBy);
}
