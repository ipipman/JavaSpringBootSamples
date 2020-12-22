package com.ipman.dubbo.attachment.sample.impl;

import com.ipman.dubbo.attachment.sample.api.AttachmentService;
import org.apache.dubbo.rpc.RpcContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.attachment.sample.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 3:41 下午
 */
public class AttachmentServiceImpl implements AttachmentService {

    @Override
    public String sayHello(String name) {

        RpcContext context = RpcContext.getContext();
        //获取客户端隐式传入的参数
        String index = (String) context.getAttachment("index");

        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name +
                ", request from consumer: " + context.getRemoteAddress());
        return "Hello " + name + ", response from provider: " + context.getLocalAddress() +
                ", attachment - index: " + index;
    }
}
