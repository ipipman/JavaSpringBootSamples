package com.ipman.dubbo.spi.sample;

import com.ipman.dubbo.spi.sample.api.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/23.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.spi.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/23 10:41 上午
 */
public class SpiConsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/spi-consumer.xml");
        context.start();

        DemoService demoService = context.getBean("demoService", DemoService.class);
        System.out.println(demoService.sayHello("ipman"));
    }
}
