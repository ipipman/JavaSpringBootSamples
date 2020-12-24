package com.ipman.dubbo.consul.sample;

import com.ipman.dubbo.consul.sample.api.DemoService;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/24.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.consul.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/24 6:25 下午
 */
public class ConsulConsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/consul-consumer.xml");
        context.start();

        DemoService demoService = context.getBean("demoService", DemoService.class);
        System.out.println(demoService.sayHello("ipman"));
    }
}
