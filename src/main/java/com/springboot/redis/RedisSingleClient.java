package com.springboot.redis;

import com.springboot.util.SerializeUtil;
import com.springboot.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

@Component
@SuppressWarnings("all")
public class RedisSingleClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisSingleClient.class);

    @Autowired
    private JedisPool jedisPool;

    /**
     * setnx 分布式锁实现
     * 设置成功，返回 1
     * 设置失败，返回 0
     * 异常返回2
     * @param key
     * @param value
     * @return
     */
    public long setnx(String key, String value) {
        long result = 1L;
        Jedis jedis = null;
        try {
            jedis = this.getResource();
            result = jedis.setnx(key,value);
            logger.debug("setnx {}", key);
        } catch (Exception e) {
            logger.error("setnx {}", key, e);
            result = 2L;
        } finally {
            this.returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置缓存,超时时间
     * 时间单位 秒
     * @param key
     * @param ttl
     */
    public void expire(String key, int ttl) {
        Jedis jedis = null;
        try {
            jedis = this.getResource();
            jedis.expire(key,ttl);
            logger.debug("expire {}", key);
        } catch (Exception e) {
            logger.error("expire {}", key, e);
        } finally {
            this.returnResource(jedis);
        }
    }

    /**
     * 设置缓存,超时时间，0为不超时
     * 时间单位 秒
     * @param key
     * @param value
     * @param cacheSeconds
     * @return 成功返回OK
     */
    public String set(String key, String value,int cacheSeconds){
        String result = null;
        Jedis jedis = null;
        try {
            jedis = this.getResource();
            result = jedis.set(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("set {} = {}", key, value);
        } catch (Exception e) {
            logger.error("set {} = {}", key, value, e);
        } finally {
            this.returnResource(jedis);
        }
        return result;
    }

    /**
     * 设置缓存,超时时间，0为不超时
     * 时间单位 秒
     * 针对Object实体，序列化存取
     * @param key
     * @param value
     * @param cacheSeconds
     * @return 成功返回OK
     */
    public String setObject(String key, Object value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(getBytesKey(key),toBytes(value));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setObject {} = {}", key, value);
        } catch (Exception e) {
            logger.error("setObject {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public String get(String key){
        String value = null;
        Jedis jedis = null;
        try {
            jedis = this.getResource();
            if (jedis.exists(key)) {
                value = jedis.get(key);
                value = StringUtil.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
                logger.debug("get {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("get {} = {}", key, value, e);
        } finally {
            this.returnResource(jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     * 针对Object实体，序列化存取
     * @param key
     * @return
     */
    public Object getObject(String key) {
        Object value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = toObject(jedis.get(getBytesKey(key)));
                logger.debug("getObject {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("getObject {} = {}", key, value, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }


    /**
     * 删除缓存 成功返回1
     * @param key
     * @return
     */
    public long remove(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = this.getResource();
            if (jedis.exists(key)){
                result = jedis.del(key);
                logger.debug("del {}", key);
            }else{
                logger.warn("del {} not exists", key);
            }
        } catch (Exception e) {
            logger.error("del {}", key, e);
        } finally {
            this.returnResource(jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     * @param key
     * @return
     */
    public long removeObject(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))){
                result = jedis.del(getBytesKey(key));
                logger.debug("delObject {}", key);
            }else{
                logger.debug("delObject {} not exists", key);
            }
        } catch (Exception e) {
            logger.error("delObject {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     * @autho 董杨炀
     * @time 2017年5月6日 上午11:13:37
     * @param key
     * @return
     */
    public boolean exists(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = this.getResource();
            result = jedis.exists(key);
            logger.debug("exists {}", key);
        } catch (Exception e) {
            logger.error("exists {}", key, e);
        } finally {
            this.returnResource(jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     * @param key
     * @return
     */
    public boolean existsObject(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.exists(getBytesKey(key));
            logger.debug("existsObject {}", key);
        } catch (Exception e) {
            logger.error("existsObject {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 获取byte[]类型Key
     * @param object
     * @return
     */
    private static byte[] getBytesKey(Object object){
        if(object instanceof String){
            return StringUtil.getBytes((String)object);
        }else{
            return SerializeUtil.fstSerialize(object);
        }
    }

    /**
     * Object转换byte[]类型
     * @param object
     * @return
     */
    private static byte[] toBytes(Object object){
        return SerializeUtil.fstSerialize(object);
    }

    /**
     * byte[]型转换Object
     * @param bytes
     * @return
     */
    private static Object toObject(byte[] bytes){
        return SerializeUtil.fstDeserialize(bytes);
    }

    /**
     * 获取资源
     * @return
     * @throws JedisException
     */
    private Jedis getResource() throws JedisException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException e) {
            returnBrokenResource(jedis);
            throw e;
        }
        return jedis;
    }

    /**
     * 归还资源
     * @param jedis
     */
    private void returnBrokenResource(Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnBrokenResource(jedis);
        }
    }

    /**
     * 释放资源
     * @param jedis
     */
    private void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
}
