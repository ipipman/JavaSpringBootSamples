package com.ipman.dubbo.attachment.sample;

import com.ipman.dubbo.attachment.sample.api.AttachmentService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.attachment.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 3:53 下午
 */
public class AttachmentConsumer {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/attachment-consumer.xml");
        AttachmentService attachmentService = context.getBean("attachmentService", AttachmentService.class);
        // 隐式传参，后面的远程调用都会隐式将这些参数发送到服务器端，类似cookie
        RpcContext.getContext().setAttachment("index", "1");

        // 业务，远程调用
        // Hello ipman, response from provider: 10.13.224.253:20880, attachment - index: 1
        System.out.println(attachmentService.sayHello("ipman"));
    }
}
