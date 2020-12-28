package com.ipman.springcloud.consul.register.sample.api;

import com.ipman.springcloud.consul.register.sample.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ipipman on 2020/12/28.
 *
 * @version V1.0
 * @Package com.ipman.springcloud.consul.register.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/28 7:05 下午
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
