package com.ipman.dubbo.xml.consume.action;

import com.ipman.dubbo.xml.api.service.IHelloService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.xml.consume.action
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 11:16 上午
 */
@Component
public class CallHelloService {

    @Resource
    private IHelloService helloService;

    //调用Dubbo服务
    public void doSayHello(){
         System.out.println(helloService.sayHello("ipman"));
    }

}
