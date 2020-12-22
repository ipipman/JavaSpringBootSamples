package com.ipman.dubbo.async.sample;

import com.ipman.dubbo.async.sample.api.AsyncService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.async.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 12:05 下午
 */
public class AsyncConsumer {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/async-consumer.xml");
        context.start();

        AsyncService asyncService = context.getBean("asyncService", AsyncService.class);
        CompletableFuture<String> future = asyncService.sayHello("ipman");
        //异步等待服务返回，第一个参数响应结果，第二个参数是异常反馈
        CountDownLatch latch = new CountDownLatch(1);
        future.whenComplete((v, t) -> {
            if (t != null) {
                System.out.println("Exception: " + t);
            } else {
                System.out.println("Response: " + v);
            }
            latch.countDown();
        });

        //在响应返回之前执行
        System.out.println("Executed before response returns");

        //避免能main线程直接退出
        latch.await();
    }
}
