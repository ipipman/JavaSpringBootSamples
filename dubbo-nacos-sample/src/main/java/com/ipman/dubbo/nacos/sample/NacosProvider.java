package com.ipman.dubbo.nacos.sample;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ipipman on 2020/12/23.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.nacos.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/23 6:51 下午
 */
public class NacosProvider {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/nacos-provider.xml");
        context.start();
        new CountDownLatch(1).await();
    }
}
