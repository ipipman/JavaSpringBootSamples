package com.ipman.dubbo.xml.provider.impl;

import com.ipman.dubbo.xml.api.service.IHelloService;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Service;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.xml.provider.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 11:02 上午
 */
@Service("helloServiceImpl")
public class HelloServiceImpl implements IHelloService {

    @Override
    public String sayHello(String name) {
        return "XML，" + name;
    }
}
