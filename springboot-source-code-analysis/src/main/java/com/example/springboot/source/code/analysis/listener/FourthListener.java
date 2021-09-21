package com.example.springboot.source.code.analysis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.annotation.Order;

/**
 * Created by ipipman on 2021/9/21.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.listener
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/9/21 4:26 下午
 */
@Order(4)
public class FourthListener implements SmartApplicationListener {

    private final static Logger logger = LoggerFactory.getLogger(FourthListener.class);

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationStartedEvent.class.isAssignableFrom(eventType)
                || ApplicationPreparedEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        logger.info("设置框架事件监听器【SmartApplicationListener】成功 : run fourthListener");
    }
}
