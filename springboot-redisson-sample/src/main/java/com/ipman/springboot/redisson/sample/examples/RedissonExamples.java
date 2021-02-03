package com.ipman.springboot.redisson.sample.examples;

import lombok.SneakyThrows;
import org.redisson.api.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * Created by ipipman on 2021/1/28.
 *
 * @version V1.0
 * @Package com.ipman.springboot.redisson.sample.examples
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/28 8:48 下午
 */
@Component
public class RedissonExamples {

    @Resource
    RedissonClient redissonClient;

    /**
     * 使用 RLock 实现 Redis 分布式锁
     * RLock 是 Java 中可重入锁的分布式实现
     */
    @SneakyThrows
    public void lock() {
        // RLock 继承了 java.util.concurrent.locks.Lock 接口
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        System.out.println("lock acquired");

        Thread t = new Thread(() -> {
            RLock lock1 = redissonClient.getLock("lock");
            lock1.lock();
            System.out.println("lock acquired by thread");
            lock1.unlock();
            System.out.println("lock released by thread");
        });
        t.start();

        System.out.println("lock sleep begin");
        Thread.sleep(1000);
        System.out.println("lock sleep end");

        lock.unlock();
        System.out.println("lock released");

        t.join();
    }

    /**
     * 使用 RAtomicLong 实现 Redis 分布式原子操作
     */
    @SneakyThrows
    public void atomicLong() {
        RAtomicLong atomicLong = redissonClient.getAtomicLong("atomicLong");

        System.out.println("Init value: " + atomicLong.get());

        atomicLong.incrementAndGet();
        System.out.println("Current value: " + atomicLong.get());

        atomicLong.addAndGet(1L);
        System.out.println("Final value: " + atomicLong.get());
    }

    /**
     * 基于 Redis 的 Redisson 分布式列表（list）结构的 RList
     * Java对象实现了 java.util.list 接口的同时，确保了元素插入时的顺序
     * 对象的最大容容量受 Redis 限制，最大数量是 4294967295 个
     */
    @SneakyThrows
    public void list() {
        RList<String> list = redissonClient.getList("list");
        list.add("ip");
        list.add("man");
        list.add("ipman");
        list.remove(-1);

        boolean contains = list.contains("ipman");
        System.out.println("List size: " + list.size());
        System.out.println("Is list contains name 'ipman': " + contains);
        list.forEach(System.out::println);
    }

    /**
     * 基于 Redis 的 Redisson 的分布式映射结构的 RMap
     * Java对象实现了 java.util.concurrent.ConcurrentMap 接口和 java.util.Map 接口
     * 与HashMap不同的是，RMap保持了元素的插入顺序，该对象的最大容量是 4294967295 个
     */
    @SneakyThrows
    public void map() {
        RMap<String, String> map = redissonClient.getMap("map");
        String prevVal = map.put("man", "2");
        // key 空闲时写入
        String currentVal = map.putIfAbsent("ipman", "3");
        // 在 Map 头部写入key
        map.fastPut("ip", "1");
        printMap(map);

        // 异步的方式获取
        RFuture<String> futurePut = map.putAsync("ipman", "4");
        RFuture<String> futureGet = map.getAsync("ipman");
        System.out.println("Map async put:" + futurePut.get());
        System.out.println("Map async get:" + futureGet.get());

    }


    public static void printMap(Map<String, String> map) {
        Set<Map.Entry<String, String>> ms = map.entrySet();
        for (Map.Entry<String, String> entry : ms) {
            System.out.println("Map key:" + entry.getKey() + " val:" + entry.getValue());
        }
    }
}
