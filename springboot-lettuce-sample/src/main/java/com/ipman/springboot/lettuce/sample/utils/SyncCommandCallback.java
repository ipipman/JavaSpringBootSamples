package com.ipman.springboot.lettuce.sample.utils;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * Created by ipipman on 2021/1/20.
 *
 * @version V1.0
 * @Package com.ipman.springboot.lettuce.sample.utils
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/20 7:02 下午
 */
@FunctionalInterface
public interface SyncCommandCallback<T> {

    //抽象方法，为了简化代码，便于传入回调函数
    T doInConnection(RedisCommands<String, String> commands);
}
