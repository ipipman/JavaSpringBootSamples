package com.ipman.springboot.redisson.sample.examples;

import lombok.SneakyThrows;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
}
