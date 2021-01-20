package com.ipman.springboot.lettuce.sample.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.Getter;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * Created by ipipman on 2021/1/20.
 *
 * @version V1.0
 * @Package com.ipman.springboot.lettuce.sample.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/20 6:49 下午
 */
@Component
public class LettucePoolConfig {

    @Resource
    RedisClient redisClient;

    //设置可分配的最大Redis实例数量
    @Value("${spring.redis.pool.maxTotal}")
    private Integer maxTotal;

    //设置最多空闲的Redis实例数量
    @Value("${spring.redis.pool.maxIdle}")
    private Integer maxIdle;

    //归还Redis实例时，检查有消息，如果失败，则销毁实例
    @Value("${spring.redis.pool.testOnReturn}")
    private Boolean testOnReturn;

    //当Redis实例处于空闲壮体啊时检查有效性，默认flase
    @Value("${spring.redis.pool.testWhileIdle}")
    private Boolean testWhileIdle;

    //Apache-Common-Pool是一个对象池，用于缓存Redis连接，
    //因为Letture本身基于Netty的异步驱动，但基于Servlet模型的同步访问时，连接池是必要的
    //连接池可以很好的复用连接，减少重复的IO消耗与RedisURI创建实例的性能消耗
    @Getter
    GenericObjectPool<StatefulRedisConnection<String, String>> redisConnectionPool;

    //Servlet初始化时先初始化Lettuce连接池
    @PostConstruct
    private void init() {
        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> redisPoolConfig
                = new GenericObjectPoolConfig<>();
        redisPoolConfig.setMaxIdle(this.maxIdle);
        redisPoolConfig.setMinIdle(0);
        redisPoolConfig.setMaxTotal(this.maxTotal);
        redisPoolConfig.setTestOnReturn(this.testOnReturn);
        redisPoolConfig.setTestWhileIdle(this.testWhileIdle);
        redisPoolConfig.setMaxWaitMillis(1000);
        this.redisConnectionPool =
                ConnectionPoolSupport.createGenericObjectPool(() -> redisClient.connect(), redisPoolConfig);
    }

    //Servlet销毁时先销毁Lettuce连接池
    @PreDestroy
    private void destroy() {
        redisConnectionPool.close();
        redisClient.shutdown();
    }
}
