package com.ipman.springboot.redis.jedis.sample.api;

import com.ipman.springboot.redis.jedis.sample.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ipipman on 2021/1/20.
 *
 * @version V1.0
 * @Package com.ipman.springboot.redis.jedis.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/20 8:46 下午
 */
@RestController
@RequestMapping("/jedis")
public class JedisController {

    @Autowired
    JedisUtil jedisUtil;

    /**
     * 使用Jedis工具类，调用Redis的Set指令
     * http://127.0.0.1:8080/lettuce/set?key=name&val=ipipman
     *
     * @param key
     * @param val
     * @return
     */
    @GetMapping("/set")
    public Object setItem(@RequestParam(name = "key", required = true) String key,
                          @RequestParam(name = "val", required = true) String val) {
        return jedisUtil.set(key, val, 0);
    }

    /**
     * 使用Jedis工具类，调用Redis的Get指令
     * http://127.0.0.1:8080/lettuce/get?key=name
     *
     * @param key
     * @return
     */
    @GetMapping("/get")
    public Object getItem(@RequestParam(name = "key", required = true) String key) {
        return jedisUtil.get(key, 0);
    }
}
