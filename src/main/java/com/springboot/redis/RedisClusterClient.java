package com.springboot.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.springboot.exception.BusinessException;
import com.springboot.util.SerializeUtil;
import com.springboot.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Jedis集群客户端操作API
 * 支持原生Set List Map String Object缓存操作
 * */
//@Component
@SuppressWarnings("all")
public class RedisClusterClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClusterClient.class);

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * setnx 分布式锁实现
     * 设置成功，返回 1
     * 设置失败，返回 0
     * @param key
     * @param value
     * @return
     */
    public long setnx(String key, String value) {
        long result = 1L;
        try {
            result = jedisCluster.setnx(key,value);
            logger.debug("setnx {}", key);
        } catch (Exception e) {
            logger.error("setnx {}", key, e);
            throw new BusinessException();
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
        try {
            jedisCluster.expire(key,ttl);
            logger.debug("expire {}", key);
        } catch (Exception e) {
            logger.error("expire {}", key, e);
        }
    }

    /**
     * 设置缓存,超时时间
     * 时间单位 秒
     * @param key
     * @param ttl
     */
    public void expireObject(String key, int ttl) {
        try {
            jedisCluster.expire(getBytesKey(key),ttl);
            logger.debug("expireObject {}", key);
        } catch (Exception e) {
            logger.error("expireObject {}", key, e);
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
        try {
            if (jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
            result = jedisCluster.set(key, value);
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("set {} = {}", key, value);
        } catch (Exception e) {
            logger.error("set {} = {}", key, value, e);
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
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                jedisCluster.del(getBytesKey(key));
            }
            result = jedisCluster.set(getBytesKey(key),toBytes(value));
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setObject {} = {}", key, value);
        } catch (Exception e) {
            logger.error("setObject {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 设置List缓存
     * 针对List<String>
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public long setList(String key, List<String> value, int cacheSeconds) {
        long result = 0;
        try {
            if (jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
            String[] values = (String[]) value.toArray(new String[value.size()]);
            result = jedisCluster.rpush(key,values);
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setList {} = {}", key, value);
        } catch (Exception e) {
            logger.error("setList {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 设置List缓存
     * 针对Object
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public long setObjectList(String key, List<Object> value, int cacheSeconds) {
        long result = 0;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                jedisCluster.del(getBytesKey(key));
            }
            List<byte[]> list = Lists.newArrayList();
            for (Object o : value){
                list.add(toBytes(o));
            }
            result = jedisCluster.rpush(getBytesKey(key), (byte[][])list.toArray(new byte[list.size()][]));
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setObjectList {} = {}", key, value);
        } catch (Exception e) {
            logger.error("setObjectList {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 设置Set缓存
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public long setSet(String key, Set<String> value, int cacheSeconds) {
        long result = 0;
        try {
            if (jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
            String[] values = (String[]) value.toArray(new String[value.size()]);
            result = jedisCluster.sadd(key, values);
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setSet {} = {}", key, value);
        } catch (Exception e) {
            logger.error("setSet {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 设置Set缓存
     * 针对Object序列化存取
     * 返回set的SIZE
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
        long result = 0;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                jedisCluster.del(key);
            }
            Set<byte[]> set = Sets.newHashSet();
            for (Object o : value){
                set.add(toBytes(o));
            }
            result = jedisCluster.sadd(getBytesKey(key), (byte[][])set.toArray(new byte[set.size()][]));
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setObjectSet {} = {}", key, value);
        } catch (Exception e) {
            logger.error("setObjectSet {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 设置Map缓存
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public  String setMap(String key, Map<String, String> value, int cacheSeconds) {
        String result = null;
        try {
            if (jedisCluster.exists(key)) {
                jedisCluster.del(key);
            }
            result = jedisCluster.hmset(key, value);
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setMap {} = {}", key, value);
        } catch (Exception e) {
            logger.error("setMap {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 设置Map缓存
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public  String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
        String result = null;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                jedisCluster.del(getBytesKey(key));
            }
            Map<byte[], byte[]> map = Maps.newHashMap();
            for (Map.Entry<String, Object> e : value.entrySet()){
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedisCluster.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
            if (cacheSeconds != 0) {
                jedisCluster.expire(key, cacheSeconds);
            }
            logger.debug("setObjectMap {} = {}", key, value);
        } catch (Exception e) {
            logger.error("setObjectMap {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     * @param key
     * @param value
     * @return
     */
    public long listAdd(String key, String... value) {
        long result = 0;
        try {
            result = jedisCluster.rpush(key, value);
            logger.debug("listAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.error("listAdd {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     * 针对Object序列化存取
     * @param key
     * @param value
     * @return
     */
    public long listObjectAdd(String key, Object... value) {
        long result = 0;
        try {
            List<byte[]> list = Lists.newArrayList();
            for (Object o : value){
                list.add(toBytes(o));
            }
            result = jedisCluster.rpush(getBytesKey(key), (byte[][])list.toArray(new byte[list.size()][]));
            logger.debug("listObjectAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.error("listObjectAdd {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     * 与setListAdd不同，只能返回此次添加的个数
     * @param key
     * @param value
     * @return
     */
    public long setAdd(String key, String... value) {
        long result = 0;
        try {
            result = jedisCluster.sadd(key, value);
            logger.debug("setSetAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.error("setSetAdd {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     * 针对Object序列化存取
     * 与setListAdd不同，只能返回此次添加的个数
     * @param key
     * @param value
     * @return
     */
    public long setObjectAdd(String key, Object... value) {
        long result = 0;
        try {
            Set<byte[]> set = Sets.newHashSet();
            for (Object o : value){
                set.add(toBytes(o));
            }
            result = jedisCluster.sadd(getBytesKey(key), (byte[][])set.toArray(new byte[set.size()][]));
            logger.debug("setSetObjectAdd {} = {}", key, value);
        } catch (Exception e) {
            logger.error("setSetObjectAdd {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     * @param key
     * @param value
     * @return
     */
    public String mapPut(String key, Map<String, String> value) {
        String result = null;
        try {
            result = jedisCluster.hmset(key, value);
            logger.debug("mapPut {} = {}", key, value);
        } catch (Exception e) {
            logger.error("mapPut {} = {}", key, value, e);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     * @param key
     * @param value
     * @return
     */
    public String mapObjectPut(String key, Map<String, Object> value) {
        String result = null;
        try {
            Map<byte[], byte[]> map = Maps.newHashMap();
            for (Map.Entry<String, Object> e : value.entrySet()){
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedisCluster.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
            logger.debug("mapObjectPut {} = {}", key, value);
        } catch (Exception e) {
            logger.error("mapObjectPut {} = {}", key, value, e);
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
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.get(key);
                value = StringUtil.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
                logger.debug("get {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("get {} = {}", key, value, e);
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
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                value = toObject(jedisCluster.get(getBytesKey(key)));
                logger.debug("getObject {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("getObject {} = {}", key, value, e);
        }
        return value;
    }

    /**
     * 获取List缓存
     * 针对List<String>
     * @param key
     * @return
     */
    public List<String> getList(String key) {
        List<String> value = null;
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.lrange(key, 0, -1);
                logger.debug("getList {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("getList {} = {}", key, value, e);
        }
        return value;
    }

    /**
     * 获取List<Object>
     * 针对Object操作
     * @param key
     * @return
     */
    public List<Object> getObjectList(String key) {
        List<Object> value = null;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                List<byte[]> list = jedisCluster.lrange(getBytesKey(key), 0, -1);
                value = Lists.newArrayList();
                for (byte[] bs : list){
                    value.add(toObject(bs));
                }
                logger.debug("getObjectList {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("getObjectList {} = {}", key, value, e);
        }
        return value;
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public  Set<String> getSet(String key) {
        Set<String> value = null;
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.smembers(key);
                logger.debug("getSet {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("getSet {} = {}", key, value, e);
        }
        return value;
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public  Set<Object> getObjectSet(String key) {
        Set<Object> value = null;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                value = Sets.newHashSet();
                Set<byte[]> set = jedisCluster.smembers(getBytesKey(key));
                for (byte[] bs : set){
                    value.add(toObject(bs));
                }
                logger.debug("getObjectSet {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("getObjectSet {} = {}", key, value, e);
        }
        return value;
    }

    /**
     * 获取Map缓存
     * @param key
     * @return
     */
    public Map<String, String> getMap(String key) {
        Map<String, String> value = null;
        try {
            if (jedisCluster.exists(key)) {
                value = jedisCluster.hgetAll(key);
                logger.debug("getMap {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("getMap {} = {}", key, value, e);
        }
        return value;
    }

    /**
     * 获取Map缓存
     * @param key
     * @return
     */
    public  Map<String, Object> getObjectMap(String key) {
        Map<String, Object> value = null;
        try {
            if (jedisCluster.exists(getBytesKey(key))) {
                value = Maps.newHashMap();
                Map<byte[], byte[]> map = jedisCluster.hgetAll(getBytesKey(key));
                for (Map.Entry<byte[], byte[]> e : map.entrySet()){
                    value.put(new String(e.getKey()), toObject(e.getValue()));
                }
                logger.debug("getObjectMap {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error("getObjectMap {} = {}", key, value, e);
        }
        return value;
    }

    /**
     * 删除缓存 成功返回1
     * @param key
     * @return
     */
    public long delete(String key) {
        long result = 0;
        try {
            if (jedisCluster.exists(key)){
                result = jedisCluster.del(key);
                logger.debug("delete {}", key);
            }else{
                logger.warn("delete {} not exists", key);
            }
        } catch (Exception e) {
            logger.error("delete {}", key, e);
        }
        return result;
    }

    /**
     * 批量删除缓存
     * @param key
     * @return
     */
    public void delete(List<String> keys) {
        long result = 0;
        try {
            for (String key :keys) {
                if (jedisCluster.exists(key)){
                    result = jedisCluster.del(key);
                    logger.debug("delete {}", key);
                }else{
                    logger.warn("delete {} not exists", key);
                }
            }
        } catch (Exception e) {
            logger.error("delete {}", keys, e);
        }
    }

    /**
     * 删除缓存
     * @param key
     * @return
     */
    public long deleteObject(String key) {
        long result = 0;
        try {
            if (jedisCluster.exists(getBytesKey(key))){
                result = jedisCluster.del(getBytesKey(key));
                logger.debug("deleteObject {}", key);
            }else{
                logger.debug("deleteObject {} not exists", key);
            }
        } catch (Exception e) {
            logger.error("deleteObject {}", key, e);
        }
        return result;
    }

    /**
     * 批量删除缓存
     * @param key
     * @return
     */
    public void deleteObject(List<String> keys) {
        long result = 0;
        try {
            for (String key :keys) {
                if (jedisCluster.exists(getBytesKey(key))){
                    result = jedisCluster.del(getBytesKey(key));
                    logger.debug("deleteObject {}", key);
                }else{
                    logger.debug("deleteObject {} not exists", key);
                }
            }
        } catch (Exception e) {
            logger.error("deleteObject {}", keys, e);
        }
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
        try {
            result = jedisCluster.exists(key);
            logger.debug("exists {}", key);
        } catch (Exception e) {
            logger.error("exists {}", key, e);
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
        try {
            result = jedisCluster.exists(getBytesKey(key));
            logger.debug("existsObject {}", key);
        } catch (Exception e) {
            logger.error("existsObject {}", key, e);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     * @param key
     * @param mapKey
     * @return
     */
    public long mapRemove(String key, String mapKey) {
        long result = 0;
        try {
            result = jedisCluster.hdel(key, mapKey);
            logger.debug("mapRemove {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.error("mapRemove {}  {}", key, mapKey, e);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     * @param key
     * @param mapKey
     * @return
     */
    public  long mapObjectRemove(String key, String mapKey) {
        long result = 0;
        try {
            result = jedisCluster.hdel(getBytesKey(key), getBytesKey(mapKey));
            logger.debug("mapObjectRemove {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.error("mapObjectRemove {}  {}", key, mapKey, e);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     * @param key
     * @param mapKey
     * @return
     */
    public  boolean mapExists(String key, String mapKey) {
        boolean result = false;
        try {
            result = jedisCluster.hexists(key, mapKey);
            logger.debug("mapExists {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.error("mapExists {}  {}", key, mapKey, e);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     * @param key
     * @param mapKey
     * @return
     */
    public  boolean mapObjectExists(String key, String mapKey) {
        boolean result = false;
        try {
            result = jedisCluster.hexists(getBytesKey(key), getBytesKey(mapKey));
            logger.debug("mapObjectExists {}  {}", key, mapKey);
        } catch (Exception e) {
            logger.error("mapObjectExists {}  {}", key, mapKey, e);
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
            return SerializeUtil.jdkserialize(object);
        }
    }

    /**
     * Object转换byte[]类型
     * @param object
     * @return
     */
    private static byte[] toBytes(Object object){
        return SerializeUtil.jdkserialize(object);
    }

    /**
     * byte[]型转换Object
     * @param bytes
     * @return
     */
    private static Object toObject(byte[] bytes){
        return SerializeUtil.jdkdeserialize(bytes);
    }

}
