package com.ipman.sample.dubbo.annotation.config;

import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by ipipman on 2020/12/17.
 *
 * @version V1.0
 * @Package com.ipman.sample.dubbo.annotation.provider.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/17 3:50 下午
 */
@Configuration
@EnableDubbo(scanBasePackages = "com.ipman.sample.dubbo.annotation.service.impl")
@PropertySource("classpath:/spring/dubbo-provider.properties")
public class ProviderConfiguration {

    @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(1000);
        return providerConfig;
    }
}
