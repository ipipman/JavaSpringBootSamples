package com.ipman.springboot.lettuce.sample.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ipipman on 2021/1/20.
 *
 * @version V1.0
 * @Package com.ipman.springboot.lettuce.sample.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/20 5:54 下午
 */
//如果标注了@Configuration，则通过SpringBoot自动扫描注解自动加载，否则需要在入口类中通过@Import手动加载
@Configuration
public class LettuceConfig {

    //Redis服务器地址
    @Value("${spring.redis.host}")
    private String host;

    //Redis服务端口
    @Value("${spring.redis.port}")
    private Integer port;

    //Redis密码
    @Value("${spring.redis.password}")
    private String password;

    //是否需要SSL
    @Value("${spring.redis.ssl}")
    private Boolean ssl;

    //Redis默认库，一共0～15
    @Value("${spring.redis.database}")
    private Integer database;

    @Bean
    public RedisClient redisClient() {
        RedisURI uri = RedisURI.Builder.redis(this.host, this.port)
                .withDatabase(this.database)
                .build();
        return RedisClient.create(uri);
    }

}
