package com.ipman.dubbo.cache.sample.service.impl;

import com.ipman.dubbo.cache.sample.service.CacheService;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.cache.sample.service
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 5:25 下午
 */
public class CacheServiceImpl implements CacheService {

    private final AtomicInteger i = new AtomicInteger();

    @Override
    public String findCache(Integer id) {
        return "request：" + id + "， response: " + i.getAndIncrement();
    }
}
