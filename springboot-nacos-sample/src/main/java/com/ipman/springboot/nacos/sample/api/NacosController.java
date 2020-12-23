package com.ipman.springboot.nacos.sample.api;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ipipman on 2020/12/23.
 *
 * @version V1.0
 * @Package com.ipman.springboot.nacos.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/23 10:33 下午
 */
@Controller
@RequestMapping("config")
//使用 @NacosPropertySource 加载 dataId 为 example 的配置源，并开启自动更新
@NacosPropertySource(dataId = "nacos.cfg.test", autoRefreshed = true)
public class NacosController {

    //通过 Nacos 的 @NacosValue 注解设置属性值
    @NacosValue(value = "${nacos.test.propertie:null}", autoRefreshed = true)
    private String testProperties;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public String get() {
        return testProperties;
    }
}
