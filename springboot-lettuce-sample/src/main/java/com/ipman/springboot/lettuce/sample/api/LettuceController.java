package com.ipman.springboot.lettuce.sample.api;

import com.ipman.springboot.lettuce.sample.utils.LettuceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ipipman on 2021/1/20.
 *
 * @version V1.0
 * @Package com.ipman.springboot.lettuce.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/20 8:36 下午
 */
@RestController
@RequestMapping("/lettuce")
public class LettuceController {

    //加载Lettuce工具类
    @Autowired
    LettuceUtil lettuceUtil;

    /**
     * 使用Lettuce工具类，调用Redis的Set指令
     * http://127.0.0.1:8080/lettuce/set?key=name&val=ipipman
     *
     * @param key
     * @param val
     * @return
     */
    @GetMapping("/set")
    public Object setItem(@RequestParam(name = "key", required = true) String key,
                          @RequestParam(name = "val", required = true) String val) {
        return lettuceUtil.set(key, val);
    }

    /**
     * 使用Lettuce工具类，调用Redis的Get指令
     * http://127.0.0.1:8080/lettuce/get?key=name
     *
     * @param key
     * @return
     */
    @GetMapping("/get")
    public Object getItem(@RequestParam(name = "key", required = true) String key) {
        return lettuceUtil.get(key);
    }
}
