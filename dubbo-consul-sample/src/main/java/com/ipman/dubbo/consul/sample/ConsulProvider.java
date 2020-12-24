package com.ipman.dubbo.consul.sample;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ipipman on 2020/12/24.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.consul.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/24 6:25 下午
 */
public class ConsulProvider {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/consul-provider.xml");
        context.start();

        new CountDownLatch(1).await();
    }
}
