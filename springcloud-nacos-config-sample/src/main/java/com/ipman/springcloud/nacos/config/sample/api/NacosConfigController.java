package com.ipman.springcloud.nacos.config.sample.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ipipman on 2020/12/30.
 *
 * @version V1.0
 * @Package com.ipman.springcloud.nacos.config.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/30 11:12 上午
 */

@Controller
@RequestMapping("config")
//开启自动更新
@RefreshScope
public class NacosConfigController {

    //通过 Nacos 的 @NacosValue 注解设置属性值
    @Value(value = "${nacos.test.propertie:null}")
    private String testProperties;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public String get() {
        return testProperties;
    }
}
