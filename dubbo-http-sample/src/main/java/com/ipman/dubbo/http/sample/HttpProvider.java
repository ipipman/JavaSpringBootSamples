package com.ipman.dubbo.http.sample;

import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.http.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 2:28 下午
 */
public class HttpProvider {

    private static TestingServer zkServer;

    @SneakyThrows
    public static void main(String[] args) {
        //启动ZK
        mockZookeeper();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/http-provider.xml");
        context.start();
    }

    //启动ZK
    public static void mockZookeeper() throws Exception{
        zkServer = new TestingServer(2181, true);
        zkServer.start();
    }

}
