package com.demo.common.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

/**
 * Jedis连接参数对象
 * <pre>
 * <b>Title：</b>JedisConnCommon.java<br/>
 * <b>@author：</b>WML<br/>
 * <b>@date：</b>2017年3月2日 - 下午4:14:51<br/>  
 * <b>@version V1.0</b></br/>
 * <b>Copyright (c) 2017 ASPire Tech.</b>   
 * </pre>
 */
public class JedisConnCommon {
	/**
	 * host ip
	 */
    private String hostName;
    
    /**
     * redis 服务器端口
     */
    private int port;
    
    /**
     * 超时时间
     */
    private int timeout;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 
     */
    private boolean usePool;
    
    /**
     * Jedis 池对象
     */
    private Pool<Jedis> pool;
    
    /**
     * JedisPool配置对象
     */
    private JedisPoolConfig poolConfig;
    
    /**
     * 编号
     */
    private int dbIndex;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUsePool() {
        return usePool;
    }

    public void setUsePool(boolean usePool) {
        this.usePool = usePool;
    }

    public Pool<Jedis> getPool() {
        return pool;
    }

    public void setPool(Pool<Jedis> pool) {
        this.pool = pool;
    }

    public JedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }
}
