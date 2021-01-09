package com.ipman.springboot.redis.jedis.sample;

import com.ipman.springboot.redis.jedis.sample.utils.JedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by ipipman on 2021/1/6.
 *
 * @version V1.0
 * @Package com.ipman.springboot.redis.jedis.sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/6 10:00 上午
 */
@SpringBootTest
class JedisTest {

    @Autowired
    private JedisUtil jedisUtil;

    @Test
    public void testCmd(){
        jedisUtil.set("name", "ipman", 0);
        jedisUtil.expire("name", 86400, 0);
        System.out.println(jedisUtil.get("name", 0));
        System.out.println(jedisUtil.ttl("name", 0));
    }

}
