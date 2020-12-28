package com.ipman.springcloud.consul.config.center.sample.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ipipman on 2020/12/28.
 *
 * @version V1.0
 * @Package com.ipman.springcloud.consul.config.center.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/28 12:03 下午
 */
@RestController
@RequestMapping("/consul")
@RefreshScope // 如果参数变化，自动刷新
public class ConsulConfigCenterController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/get")
    public Object getItem() {
        return configInfo;
    }
}
