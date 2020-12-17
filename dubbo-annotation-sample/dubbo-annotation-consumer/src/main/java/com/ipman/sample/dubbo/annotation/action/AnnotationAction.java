package com.ipman.sample.dubbo.annotation.action;

import com.ipman.sample.dubbo.annotation.service.AnnotationConstants;
import com.ipman.sample.dubbo.annotation.service.IHelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * Created by ipipman on 2020/12/17.
 *
 * @version V1.0
 * @Package com.ipman.sample.dubbo.anntation.consumer.action
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/17 4:21 下午
 */
@Component("annotationAction")
public class AnnotationAction {

    //消费者指定引用的提供者接口服务
    @Reference(interfaceClass = IHelloService.class,
            version = AnnotationConstants.VERSION,
            timeout = 1000)
//            methods = {@Method(name = "sayHello", timeout = 3000, retries = 1)})
    public IHelloService helloService;


    //测试Provider
    public String doSayHello() {
        try {
            return helloService.sayHello("ipman");
        } catch (Exception e) {
            e.printStackTrace();
            return "Throw Exception";
        }
    }
}
