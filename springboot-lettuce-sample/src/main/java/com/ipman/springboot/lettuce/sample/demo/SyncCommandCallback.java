package com.ipman.springboot.lettuce.sample.demo;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * Created by ipipman on 2021/1/20.
 *
 * @version V1.0
 * @Package com.ipman.springboot.lettuce.sample.demo
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/20 3:36 下午
 */
@FunctionalInterface
public interface SyncCommandCallback<T> {

    T doInConnection(RedisCommands<String, String> command);
}
