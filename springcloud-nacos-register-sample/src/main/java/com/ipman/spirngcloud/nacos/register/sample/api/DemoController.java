package com.ipman.spirngcloud.nacos.register.sample.api;

import com.ipman.spirngcloud.nacos.register.sample.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ipipman on 2020/12/30.
 *
 * @version V1.0
 * @Package com.ipman.spirngcloud.nacos.register.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/30 3:14 下午
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    DemoService demoService;

    @GetMapping("hello")
    public Object echo(String name){
        return demoService.sayHello(name);
    }
}
