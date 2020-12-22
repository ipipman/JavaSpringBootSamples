package com.ipman.dubbo.mock.sample.impl;

import com.ipman.dubbo.mock.sample.api.MockService;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.mock.sample.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 8:30 下午
 */
public class MockServiceImpl implements MockService {

    @Override
    public String sayHello(String name) {
        try {
            // 休眠5秒会导致客户端的TimeoutException，并且会调用mock impl
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello " + name;
    }
}
