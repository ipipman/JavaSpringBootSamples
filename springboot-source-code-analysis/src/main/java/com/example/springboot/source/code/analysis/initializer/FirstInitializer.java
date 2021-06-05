package com.example.springboot.source.code.analysis.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * FirstInitializer 系统初始化器
 *
 * @author ipipman
 * @version V1.0
 * @date 2021/6/5
 * @date 2021/6/5 8:20 下午
 */
@Order(1) // 设置执行顺序
public class FirstInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger =  LoggerFactory.getLogger(FirstInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        // 获取系统属性
        ConfigurableEnvironment environment =
                configurableApplicationContext.getEnvironment();

        // 设置自定义属性
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("key1", "value1");

        // 添加自定义属性
        MapPropertySource mapPropertySource = new MapPropertySource("firstInitializer", attributeMap);
        environment.getPropertySources().addLast(mapPropertySource);
        logger.info("设置框架初始化器【ApplicationContextInitializer】成功 : run firstInitializer");
    }
}
