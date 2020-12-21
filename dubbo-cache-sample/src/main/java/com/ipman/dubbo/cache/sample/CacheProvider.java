package com.ipman.dubbo.cache.sample;

import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.cache.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 5:34 下午
 */
public class CacheProvider {

    private static TestingServer zkServer;

    public static void main(String[] args) {
        //启动ZK
        mockZookeeperServer();

        //启动Dobbo生产者
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/cache-provider.xml");
        context.start();
    }

    //启动ZK
    @SneakyThrows
    public static void mockZookeeperServer() {
        zkServer = new TestingServer(2181, true);
        zkServer.start();
    }
}
