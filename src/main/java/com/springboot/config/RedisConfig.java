package com.springboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Configuration
@PropertySource(value= "classpath:redis.properties")
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean(name= "jedis.pool")
    @Autowired
    //@Value("${jedis.pool.password}")String password
    public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,
                               @Value("${jedis.pool.host}")String host,
                               @Value("${jedis.pool.port}")int port
                               ) {
        logger.info("redis url ："+host+":"+port);
        return new JedisPool(config, host, port, Protocol.DEFAULT_TIMEOUT);
    }

    @Bean(name= "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig (@Value("${jedis.pool.config.maxTotal}")int maxTotal,
                                            @Value("${jedis.pool.config.maxIdle}")int maxIdle,
                                            @Value("${jedis.pool.config.maxWaitMillis}")int maxWaitMillis,
                                            @Value("${jedis.pool.config.minIdle}")int minIdle,
                                            @Value("${jedis.pool.config.minEvictableIdleTimeMillis}")int minEvictableIdleTimeMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
        config.setBlockWhenExhausted(false);
        config.setTimeBetweenEvictionRunsMillis(-1);
        config.setTestOnBorrow(false);
        config.setTestWhileIdle(false);
        config.setNumTestsPerEvictionRun(3);
        return config;
    }
}
