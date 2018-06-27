package com.springboot.redis;

import com.springboot.enums.MutexElement;
import com.springboot.exception.BusinessException;
import com.springboot.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 业务互斥锁服务接口
 */
@SuppressWarnings("all")
@Service("businessLockService")
public class BusinessLockServiceImpl implements BusinessLockService{

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessLockServiceImpl.class);

    @Autowired
    private RedisSingleClient redisSingleClient;

    @Override
    public boolean lock(MutexElement mutex, int timeout) {
        if (mutex == null || mutex.getType() == null || StringUtil.isEmpty(mutex.getUniqueNum())) {
            throw new BusinessException("The mutex parameter is empty");
        }
        String key = mutex.getType().getPrefix() + mutex.getUniqueNum();
        String value = mutex.getType().getName();
        long nano = System.nanoTime();
        try {
            do {
                //使用setnx模拟锁
                Long i = redisSingleClient.setnx(key, value);
                if (i == 1) {   //setnx成功，获得锁
                    redisSingleClient.expire(key, mutex.getTtl());
                    LOGGER.debug("get lock, key: " + key + " , expire in " + mutex.getTtl() + " seconds.");
                    return true;
                } else {//已存在锁
                    String desc = redisSingleClient.get(key);
                    LOGGER.debug("key: " + key + " locked by another business：" + desc);
                }
                if (timeout == 0) { //非阻塞调用则退出
                    break;
                }
                Thread.sleep(1000); //每秒访问一次
            } while ((System.nanoTime() - nano) < timeout * 1000 * 1000 * 1000);
            // 得不到锁，返回失败
            return false;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public void unlock(MutexElement mutex) {
        if (mutex == null || mutex.getType() == null || StringUtil.isEmpty(mutex.getUniqueNum())) {
            throw new BusinessException("The mutex parameter is empty");
        }
        String key = mutex.getType().getPrefix() + mutex.getUniqueNum();
        try {
            if(redisSingleClient.exists(key)){
                redisSingleClient.delete(key);
                LOGGER.debug("release lock, key :" + key);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean lock(List<MutexElement> mutexes) {
        List<String> failLocks = new ArrayList<>();
        if (mutexes.isEmpty()) {
            throw new BusinessException("The mutex parameter is empty");
        }
        Map<String,String> map = new HashMap<>();
        for (MutexElement mutex : mutexes) {
            if (mutex == null || mutex.getType() == null || StringUtil.isBlank(mutex.getUniqueNum())) {
                throw new BusinessException("The mutex parameter is empty");
            }
            String key = mutex.getType().getPrefix() + mutex.getUniqueNum();
            String value = mutex.getType().getName();
            map.put(key,value);
        }
        try {
            Set<String> keySet = map.keySet();
            for (String key:keySet) {
                Long i = redisSingleClient.setnx(key, map.get(key));
                if(i != 1){
                    String desc = redisSingleClient.get(key);
                    LOGGER.debug("key: " + key + " locked by another business：" + desc);
                    failLocks.add(key.split("_")[1]);
                }
            }
            //如果存在锁定失败的，返回false，此时要解锁已被锁定的
            if(!failLocks.isEmpty()){
                for (int i = mutexes.size() - 1; i >= 0; i--) {
                    for (int j = 0; j < failLocks.size() ; j++) {
                        if(mutexes.get(i).getUniqueNum().equals(failLocks.get(j))){
                            mutexes.remove(i);
                            break;
                        }
                    }
                }
                if(!mutexes.isEmpty()){
                    this.unlock(mutexes);
                }
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public void unlock(List<MutexElement> mutexes) {
        if (mutexes.isEmpty()) {
            throw new BusinessException("The mutex parameter is empty");
        }
        List<String> keys = new ArrayList<String>();
        for (MutexElement mutex : mutexes) {
            if (mutex == null || mutex.getType() == null || StringUtil.isBlank(mutex.getUniqueNum())) {
                throw new BusinessException("The mutex parameter is empty");
            }
            String key = mutex.getType().getPrefix() + mutex.getUniqueNum();
            keys.add(key);
        }
        try {
            redisSingleClient.delete(keys);
            LOGGER.debug("release lock, keys :" + keys);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
