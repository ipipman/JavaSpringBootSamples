package com.example.springboot.source.code.analysis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

/**
 * Created by ipipman on 2021/9/21.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.listener
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/9/21 4:19 下午
 */
@Order(2)
public class SecondListener implements ApplicationListener<ApplicationStartedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SecondListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        logger.info("设置框架事件监听器【ApplicationListener】成功 : run secondListener");
    }
}
