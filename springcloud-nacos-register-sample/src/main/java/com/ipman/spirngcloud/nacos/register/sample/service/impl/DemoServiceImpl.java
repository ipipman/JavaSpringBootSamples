package com.ipman.spirngcloud.nacos.register.sample.service.impl;

import com.ipman.spirngcloud.nacos.register.sample.ServerConfig;
import com.ipman.spirngcloud.nacos.register.sample.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ipipman on 2020/12/30.
 *
 * @version V1.0
 * @Package com.ipman.spirngcloud.nacos.register.sample.service.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/30 3:13 下午
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    ServerConfig serverConfig;

    @Override
    public String sayHello(String name) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name +
                ", request from consumer: " + serverConfig.getUrl());
        return "Hello " + name + ", response from provider: " + serverConfig.getUrl();
    }

}
