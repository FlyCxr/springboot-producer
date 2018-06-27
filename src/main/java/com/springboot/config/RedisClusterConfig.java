package com.springboot.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import redis.clients.jedis.*;
import java.util.LinkedHashSet;
import java.util.Set;

//@Configuration
//@PropertySource(value= "classpath:redis.properties")
public class RedisClusterConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisClusterConfig.class);

    private static Set<HostAndPort> nodeSet;

    @Value("${redis.cluster.nodes}")
    public void setNodeSet(String nodes) {
        try {
            Set<HostAndPort> hostAndPortSet = new LinkedHashSet();
            String[] hostAndPorts = nodes.split(",");
            for (String hap : hostAndPorts) {
                String ip = hap.split(":")[0];
                int port = Integer.parseInt(hap.split(":")[1]);
                HostAndPort hostAndPort = new HostAndPort(ip, port);
                hostAndPortSet.add(hostAndPort);
            }
            nodeSet = hostAndPortSet;
        } catch (Exception e) {
            logger.error("redis cluster config nodes is error");
            throw e;
        }
    }

    @Bean(name= "redis.cluster.jedisCluster")
    @Autowired
    public JedisCluster jedisCluster(@Qualifier("redis.cluster.genericObjectPoolConfig") GenericObjectPoolConfig config,
                                     @Value("${redis.cluster.connectionTimeout}")int connectionTimeout,
                                     @Value("${redis.cluster.soTimeout}")int soTimeout,
                                     @Value("${redis.cluster.maxRedirections}")int maxRedirections) {
        return new JedisCluster(RedisClusterConfig.nodeSet, connectionTimeout, soTimeout, maxRedirections,config);
    }

    @Bean(name= "redis.cluster.genericObjectPoolConfig")
    public GenericObjectPoolConfig genericObjectPoolConfig (@Value("${jedis.pool.config.maxTotal}")int maxTotal,
                                                    @Value("${jedis.pool.config.maxIdle}")int maxIdle,
                                                    @Value("${jedis.pool.config.maxWaitMillis}")int maxWaitMillis,
                                                    @Value("${jedis.pool.config.minIdle}")int minIdle,
                                                    @Value("${jedis.pool.config.minEvictableIdleTimeMillis}")int minEvictableIdleTimeMillis) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
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
