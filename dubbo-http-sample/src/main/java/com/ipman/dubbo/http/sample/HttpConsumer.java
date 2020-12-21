package com.ipman.dubbo.http.sample;

import com.ipman.dubbo.http.sample.api.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.http.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 2:25 下午
 */
public class HttpConsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/http-consumer.xml");
        context.start();

        DemoService demoService = (DemoService) context.getBean("demoService");
        String result = demoService.sayHello("ipman");
        System.out.println(result);
    }
}
