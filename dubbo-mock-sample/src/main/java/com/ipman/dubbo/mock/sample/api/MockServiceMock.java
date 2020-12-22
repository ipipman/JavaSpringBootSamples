package com.ipman.dubbo.mock.sample.api;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.mock.sample.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 8:33 下午
 */
public class MockServiceMock implements MockService {

    @Override
    public String sayHello(String name) {
        return "Response from Mock sayHello，" + name;
    }
}
