package com.ipman.dubbo.notify.sample.impl;

import com.ipman.dubbo.notify.sample.api.DemoService;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.notify.sample.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 10:15 上午
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(int id) {
        if (id > 5) {
            throw new RuntimeException("runtime exception from id > 5");
        }
        return "sayHello.ok" + id;
    }
}
