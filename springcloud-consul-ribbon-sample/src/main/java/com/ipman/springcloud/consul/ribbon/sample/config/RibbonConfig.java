package com.ipman.springcloud.consul.ribbon.sample.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ipipman on 2020/12/29.
 *
 * @version V1.0
 * @Package com.ipman.springcloud.consul.ribbon.sample.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/29 10:56 上午
 */
@Configuration
public class RibbonConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
