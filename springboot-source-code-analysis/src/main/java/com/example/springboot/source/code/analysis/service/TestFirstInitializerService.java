package com.example.springboot.source.code.analysis.service;

import com.sun.istack.internal.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 测试框架初始化器，获取自定义的环境属性
 *
 * @author ipipman
 * @version V1.0
 * @date 2021/6/5
 * @date 2021/6/5 8:35 下午
 */
@Component
public class TestFirstInitializerService implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(TestFirstInitializerService.class);

    /**
     * 框架应用上下文
     */
    private ApplicationContext applicationContext;

    /**
     * 获取当前框架上下文
     */
    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取在框架初始化器阶段设置的配置信息
     */
    public String getCustomEnvironmentProperty() {
        return Optional.ofNullable(applicationContext.getEnvironment().getProperty("key1")).orElse(null);
    }
}
