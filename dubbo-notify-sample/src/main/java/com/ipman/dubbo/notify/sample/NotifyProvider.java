package com.ipman.dubbo.notify.sample;

import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.notify.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 10:24 上午
 */
public class NotifyProvider {

    private static TestingServer zkServer;

    public static void main(String[] args) {
        //开启ZK
        mockZookeeper();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/notify-provider.xml");
        context.start();
    }

    //开启ZK
    @SneakyThrows
    public static void mockZookeeper(){
        zkServer = new TestingServer(2181, true);
        zkServer.start();
    }
}
