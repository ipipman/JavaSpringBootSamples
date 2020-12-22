package com.ipman.dubbo.notify.sample;

import com.ipman.dubbo.notify.sample.api.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.notify.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 10:24 上午
 */
public class NotifyConsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/notify-consumer.xml");

        DemoService demoService = context.getBean("demoService", DemoService.class);

        //onReturn：name=sayHello.ok，id=1
        demoService.sayHello(1);

        //onThrow：ex=java.lang.RuntimeException: runtime exception from id > 5，id=6
        demoService.sayHello(6);
    }
}
