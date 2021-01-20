package com.ipman.springboot.lettuce.sample.demo;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import lombok.SneakyThrows;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by ipipman on 2021/1/20.
 *
 * @version V1.0
 * @Package com.ipman.springboot.lettuce.sample.demo
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/20 3:39 下午
 */
public class LettuceUtils {

    GenericObjectPool<StatefulRedisConnection<String, String>> redisPool;

    @SneakyThrows
    public <T> T executeSync(SyncCommandCallback<T> callback) {
        StatefulRedisConnection<String, String> connection = redisPool.borrowObject();
        connection.setAutoFlushCommands(true);

        RedisCommands<String, String> commands = connection.sync();
        return callback.doInConnection(commands);
    }

    public String get(String key){
        return executeSync(command -> command.get(key));
    }




}
