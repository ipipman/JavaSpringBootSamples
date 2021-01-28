package com.ipman.springboot.redisson.sample.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ipipman on 2021/1/28.
 *
 * @version V1.0
 * @Package com.ipman.springboot.redisson.sample.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/28 6:09 下午
 */
@Configuration
public class RedissonConfig {

    private int timeout = 3000;

    private String address = "redis://10.211.55.6:6379";

    private String password;

    private int database = 0;

    private int connectionPoolSize = 64;

    private int connectionMinimumIdleSize=10;

    private int slaveConnectionPoolSize = 250;

    private int masterConnectionPoolSize = 250;

    private String[] sentinelAddresses;

    private String masterName;

    @Bean
    public RedissonClient redissonSingle() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setTimeout(timeout)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize);
        return Redisson.create(config);
    }



}
