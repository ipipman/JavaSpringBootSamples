package com.ipman.dubbo.callback.sample;

import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.callback.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 9:27 下午
 */
public class CallbackProvider {

    private static TestingServer zkServer;

    public static void main(String[] args) {
        //启动ZK
        mockZookeeper();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/callback-provider.xml");
        context.start();
    }

    //启动ZK
    @SneakyThrows
    public static void mockZookeeper(){
        zkServer = new TestingServer(2181, true);
        zkServer.start();
    }
}
