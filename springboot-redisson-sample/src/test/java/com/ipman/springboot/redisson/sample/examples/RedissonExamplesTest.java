package com.ipman.springboot.redisson.sample.examples;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by ipipman on 2021/1/28.
 *
 * @version V1.0
 * @Package com.ipman.springboot.redisson.sample.examples
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/28 8:57 下午
 */
@SpringBootTest
public class RedissonExamplesTest {

    @Autowired
    RedissonExamples redissonExamples;

    /**
     * 使用 RLock 实现 Redis 分布式锁测试
     */
    @Test
    public void testLock() {
        redissonExamples.lock();
    }

    /**
     * 使用 RAtomicLong 实现 Redis 分布式原子操作
     */
    @Test
    public void testAtomicLong() {
        redissonExamples.atomicLong();
    }

    /**
     * 使用 RList 实现 Java List 集合的分布式并发操作
     */
    @Test
    public void testList() {
        redissonExamples.list();
    }

    /**
     * 使用 RMap 实现 Java Map 集合的分布式并发操作
     */
    @Test
    public void testMap() {
        redissonExamples.map();
    }
}
