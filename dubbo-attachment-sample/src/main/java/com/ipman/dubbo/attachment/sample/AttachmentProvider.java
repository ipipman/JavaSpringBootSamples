package com.ipman.dubbo.attachment.sample;

import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.attachment.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 3:49 下午
 */
public class AttachmentProvider {

    private static TestingServer zkServer;

    public static void main(String[] args) {
        //启动ZK
        mockZookeeper();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/attachment-provider.xml");
        context.start();
    }

    //启动ZK
    @SneakyThrows
    public static void mockZookeeper() {
        zkServer = new TestingServer(2181, true);
        zkServer.start();
    }
}
