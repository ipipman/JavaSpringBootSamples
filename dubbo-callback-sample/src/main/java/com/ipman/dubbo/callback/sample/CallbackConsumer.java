package com.ipman.dubbo.callback.sample;

import com.ipman.dubbo.callback.sample.api.CallbackService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.callback.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 9:26 下午
 */
public class CallbackConsumer {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/callback-consumer.xml");
        context.start();

        CountDownLatch latch = new CountDownLatch(1);
        CallbackService callbackService = context.getBean("callbackService", CallbackService.class);
        callbackService.addListener("ipman", msg -> {
            System.out.println("callback：" + msg);
            latch.countDown();
        });

        latch.await(5000, TimeUnit.MILLISECONDS);
    }
}
