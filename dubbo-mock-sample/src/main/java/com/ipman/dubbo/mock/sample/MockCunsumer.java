package com.ipman.dubbo.mock.sample;

import com.ipman.dubbo.mock.sample.api.MockService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.mock.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 8:35 下午
 */
public class MockCunsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/mock-consumer.xml");
        context.start();

        MockService mockService = context.getBean("mockService", MockService.class);
        System.out.println(mockService.sayHello("ipman"));
    }
}
