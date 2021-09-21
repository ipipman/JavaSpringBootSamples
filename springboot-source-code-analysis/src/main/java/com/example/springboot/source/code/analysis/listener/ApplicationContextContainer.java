package com.example.springboot.source.code.analysis.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Created by ipipman on 2021/9/21.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.listener
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/9/21 5:49 下午
 */
@Component
@Order(1)
public class ApplicationContextContainer implements ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;

    private static ApplicationContextContainer applicationContextContainer;

    @Override
    public void onApplicationEvent(@Nullable ContextRefreshedEvent event) {
        if (event != null && applicationContext == null) {
            synchronized (ApplicationContextContainer.class) {
                if (applicationContext == null) {
                    applicationContext = event.getApplicationContext();
                }
            }
        }
        applicationContextContainer = this;
    }

    public static ApplicationContextContainer getInstance() {
        return applicationContextContainer;
    }

    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
