package com.ipman.springcloud.consul.ribbon.sample.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ipipman on 2020/12/29.
 *
 * @version V1.0
 * @Package com.ipman.springcloud.consul.ribbon.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/29 11:13 上午
 */
@RestController
@RequestMapping("/consul")
public class DemoController {

    @Value("${service.url.consul.provider.service}")
    private String serviceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/ribbon")
    public Object goService(String name) {
        return restTemplate.getForObject(serviceUrl + "/demo/hello?name={1}", String.class, name);
    }
}
