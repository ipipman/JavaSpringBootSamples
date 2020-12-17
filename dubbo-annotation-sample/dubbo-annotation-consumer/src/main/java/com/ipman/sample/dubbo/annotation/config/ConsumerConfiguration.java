package com.ipman.sample.dubbo.annotation.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by ipipman on 2020/12/17.
 *
 * @version V1.0
 * @Package com.ipman.sample.dubbo.anntation.consumer.config
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/17 4:19 下午
 */
@Configuration
@EnableDubbo(scanBasePackages = "com.ipman.sample.dubbo.annotation.action")
@PropertySource("classpath:/spring/dubbo-consumer.properties")
@ComponentScan(value = "com.ipman.sample.dubbo.annotation.action")
public class ConsumerConfiguration {

}
