package com.ipman.dubbo.cache.sample;

import com.ipman.dubbo.cache.sample.service.CacheService;
import lombok.SneakyThrows;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.processing.SupportedSourceVersion;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.cache.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 5:34 下午
 */
public class CacheConsumer {

    @SneakyThrows
    public static void main(String[] args) {
        //启动消费者
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/cache-consumer.xml");
        CacheService cacheService = (CacheService) context.getBean("cacheService");

        //对比结果：True，命中缓存
        String value = cacheService.findCache(0);
        System.out.println(value.equals(cacheService.findCache(0)));

        //默认使用LRU淘汰算法，默认缓存Size是1000，如果调用1001次缓存则失效
        for (int i = 1; i <= 1001; i++) {
            cacheService.findCache(i);
        }

        //对比结果：Flase，缓存失效
        System.out.println(value.equals(cacheService.findCache(0)));
    }
}
