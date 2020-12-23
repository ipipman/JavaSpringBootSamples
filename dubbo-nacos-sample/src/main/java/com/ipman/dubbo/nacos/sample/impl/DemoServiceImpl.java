package com.ipman.dubbo.nacos.sample.impl;

import com.ipman.dubbo.nacos.sample.api.DemoService;
import org.apache.dubbo.rpc.RpcContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ipipman on 2020/12/23.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.nacos.sample.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/23 6:49 下午
 */
public class DemoServiceImpl  implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name +
                ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hello " + name + ", response from provider: " + RpcContext.getContext().getLocalAddress();
    }

}
