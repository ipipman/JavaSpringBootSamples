package com.ipman.dubbo.spi.sample;

import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by ipipman on 2020/12/23.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.spi.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/23 10:40 上午
 */
public class SpiProvider {

    private static TestingServer zkServer;

    @SneakyThrows
    public static void main(String[] args) {
        //启动ZK
        mockZookeeper();

        //启动Provider
        startProvider();
    }

    //启动多个Provider，用于LoadBalance扩展测试
    @SneakyThrows
    public static void startProvider() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("spring/spi-provider.xml");
        context.start();
    }

    //启动ZK
    @SneakyThrows
    public static void mockZookeeper() {
        zkServer = new TestingServer(2181, true);
        zkServer.start();
    }
}
