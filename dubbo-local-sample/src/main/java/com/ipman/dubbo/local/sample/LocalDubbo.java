package com.ipman.dubbo.local.sample;

import com.ipman.dubbo.local.sample.api.DemoService;
import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.local.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 2:39 下午
 */
public class LocalDubbo {

    private static TestingServer zkServer;

    public static void main(String[] args) {
        //启动ZK
        mockZookeeper();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/local-dubbo.xml");
        context.start();

        DemoService demoService = context.getBean("demoService", DemoService.class);

        //Hello ipman, response from provider: 127.0.0.1:0
        System.out.println(demoService.sayHello("ipman"));
    }

    //启动ZK
    @SneakyThrows
    public static void mockZookeeper() {
        zkServer = new TestingServer(2181, true);
        zkServer.start();
    }
}
