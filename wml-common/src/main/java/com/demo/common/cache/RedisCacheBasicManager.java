package com.demo.common.cache;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis cache 基础操作
 * <pre>
 * <b>Title：</b>RedisCacheBasicManager.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月2日 - 下午4:32:01<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RedisCacheBasicManager extends StringRedisTemplate implements ICacheManager,DisposableBean {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisConnectionFactory jedisConnectionFactory;

    public RedisCacheBasicManager(int indexDb, JedisConnCommon jedisConnCommon){
        jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(jedisConnCommon.getHostName());
        jedisConnectionFactory.setPort(jedisConnCommon.getPort());
        jedisConnectionFactory.setPoolConfig(jedisConnCommon.getPoolConfig());
        jedisConnectionFactory.setDatabase(indexDb);
        jedisConnectionFactory.afterPropertiesSet();
        this.setConnectionFactory(jedisConnectionFactory);
    }

    @Override
    public String get(String key) {
        try {
            return this.opsForValue().get(key);
        }catch (Exception e){
            logger.error("redis获取key=[{}]值失败",key,e);
            return  null;
        }
    }

    @Override
    public boolean set(String key, String value) {
        try {
            this.opsForValue().set(key,value);
        }catch (Exception e){
            logger.error("redis插入值key=[{}],value=[{}]时出错",key,value,e);
            return  false;
        }
        return true;
    }

    @Override
    public void remove(String key) {
        try {
            this.delete(key);
        }catch (Exception e){
            logger.error("redis删除key=[{}]，失败",key,e);
        }

    }

    @Override
    public boolean set(String key, String value, int timeout) {
        try {
            this.opsForValue().set(key,value,timeout, TimeUnit.SECONDS);
        }catch (Exception e){
            logger.error("redis插入值key=[{}],value=[{}],过期时间timeout=[{}]秒时出错",key,value,timeout,e);
            return  false;
        }
        return true;
    }

	@Override
    public int size() {
        try {
            Long dbSize = (Long) this.execute(new RedisCallback() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.dbSize();
                }
            });
            return dbSize.intValue();
        }catch (Exception e){
            logger.error("获取集群库中存储个数失败,返回默认值0",e);
            return  0;
        }
    }

    @SuppressWarnings("finally")
	@Override
    public Map<String, String> getMulti(Collection<String> keys) {
        Map<String,String> map = new HashMap<String, String>();
        if (keys==null||keys.size()==0){
            return  map;
        }
        try {
            List<String> valueList = this.opsForValue().multiGet(keys);
            List<String> keyList = (List<String>)keys;
            String key = null;
            String value = null;
            if (valueList!=null&&valueList.size()>0){
                for (int i=0;i<valueList.size();i++){
                    key = keyList.get(i);
                    value = valueList.get(i);
                    map.put(key,value);
                }
            }
        }catch (Exception e){
                logger.error("批量获取redis值失败,keys=[{}]",keys,e);
        }finally {
            return  map;
        }
    }

    @Override
    public void clear() {
        this.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.flushDb();
                return null;
            }
        });
    }

    @Override
    public long incr(String key, int incrBy) {
        try {
            return this.opsForValue().increment(key,incrBy);
        }catch (Exception e){
            logger.error("redis key=[{}],incrBy=[{}]自增长失败,将返回0",key,incrBy,e);
            return 0l;
        }
    }

    @Override
    public long incrExpir(final String key, final int incrBy, final int timeOut) {
        Long incrResult = 0l;
        try {
            List<Object> list = this.executePipelined(new RedisCallback<List<Object>>() {
                @Override
                public List<Object> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    redisConnection.incrBy(key.getBytes(),incrBy);
                    redisConnection.expire(key.getBytes(),timeOut);
                    return null;
                }
            });
            if (list!=null&&list.size()>0){
                incrResult = (Long) list.get(0);
            }
        }catch (Exception e){
            logger.error("redis key=[{}],decrBy=[{}],timeout=[{}]自增失败，将返回0",key,incrBy,timeOut,e);
        }
        return incrResult;
    }

    @Override
    public void mutiIncrExpir(final List<List<Object>> incrs) {
        try {
            this.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    for (List<Object> objects:incrs){
                        String key = (String) objects.get(0);
                        Integer incrBy = (Integer) objects.get(1);
//                        Integer timeOut = (Integer) objects.get(2);
                        redisConnection.incrBy(key.getBytes(),incrBy);
//                        redisConnection.expire(key.getBytes(),timeOut);
                    }
                    return true;
                }
            },false,true);
        }catch (Exception e){
            logger.error("redis key=[{}],decrBy=[{}],timeout=[{}]自增失败",e);
        }
    }

    @Override
    public long decr(final String key, final int decrBy) {
        try {
            return (Long)this.execute(new RedisCallback() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.decrBy(key.getBytes(),decrBy);
                }
            });
        }catch (Exception e){
            logger.error("redis key=[{}],decrBy=[{}]自减少失败,将返回0",key,decrBy,e);
            return 0l;
        }
    }

    @Override
    public void expirTime(String key, int timeout) {
        this.expire(key,timeout,TimeUnit.SECONDS);
    }

    @Override
    public void expirAfter1970(final String key, final String value, final long timeout) {
        this.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set(key.getBytes(),value.getBytes());
                redisConnection.expireAt(key.getBytes(),timeout);
               return false;
            }
        },false,true);
    }

    @Override
    public void destroy() throws Exception {
        jedisConnectionFactory.destroy();
    }
    
    @Override
    public void hincr(final String key,final Map<String,Integer> kvMap,final long expire){
    	this.execute(new RedisCallback<Boolean>() {
    		@Override
            public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
            	 for(String kv:kvMap.keySet()){
            		 conn.hIncrBy(key.getBytes(),kv.getBytes(),kvMap.get(kv).longValue());
            		 if(expire > 0) conn.expire(kv.getBytes(),expire);
	             }
               return false;
            }

        },false,true);
    }

	@Override
	public Map<String, String> hgetAll(final String key) {
		final Map<String, String> map = new HashMap<String, String>();
		Map<String, String> resultMap = this.execute(new RedisCallback<Map<String, String>>() {
            @Override
            public Map<String, String> doInRedis(RedisConnection conn) throws DataAccessException {
            	Map<byte[], byte[]> hGetAll = conn.hGetAll(key.getBytes());
            	for (byte[] key : hGetAll.keySet()) {
            		try {
						map.put(new String(key,"utf-8"),new String(hGetAll.get(key),"utf-8"));
					} catch (UnsupportedEncodingException e) {
						logger.error("hgetAll key={}",key,e);
					}
				}
               return map;
            }
        });
		return resultMap;
	}

	@Override
	public long hincr(final String key, final String fieldKey, final long incrBy) {
		this.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection conn) throws DataAccessException {
	           return conn.hIncrBy(key.getBytes(),fieldKey.getBytes(),incrBy);
            }
        },false,true);
		return 0l;
	}
}
