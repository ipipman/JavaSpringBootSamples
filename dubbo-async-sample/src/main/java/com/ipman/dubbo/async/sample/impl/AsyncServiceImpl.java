package com.ipman.dubbo.async.sample.impl;

import com.ipman.dubbo.async.sample.api.AsyncService;

import java.util.concurrent.CompletableFuture;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.async.sample.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 11:33 上午
 */
public class AsyncServiceImpl implements AsyncService {

    @Override
    public CompletableFuture<String> sayHello(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello，" + name;
        });
    }
}
