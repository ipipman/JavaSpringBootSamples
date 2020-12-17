package com.ipman.sample.dubbo.annotation.service.impl;

import com.ipman.sample.dubbo.annotation.service.AnnotationConstants;
import com.ipman.sample.dubbo.annotation.service.IHelloService;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.Service;

/**
 * Created by ipipman on 2020/12/17.
 *
 * @version V1.0
 * @Package com.ipman.sample.dubbo.annotation.provider.service.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/17 3:52 下午
 */
@Service(version = AnnotationConstants.VERSION)
public class HelloServiceImpl implements IHelloService {

    /**
     * Hello Service
     *
     * @param name
     * @return
     */
    @Override
    @SneakyThrows
    public String sayHello(String name) {
        System.out.println("provider received invoke of sayHello: " + name);
        Thread.sleep(300);
        return "Annotation, hello " + name;
    }
}
