package com.ipman.springboot.lettuce.sample.utils;

import com.ipman.springboot.lettuce.sample.config.LettucePoolConfig;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ipipman on 2021/1/20.
 *
 * @version V1.0
 * @Package com.ipman.springboot.lettuce.sample.utils
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/1/20 7:04 下午
 */
@Component
@Slf4j
public class LettuceUtil {

    @Autowired
    LettucePoolConfig lettucePoolConfig;

    //编写executeSync方法，在方法中，获取Redis连接，利用Callback操作Redis，最后释放连接，并返回结果
    //这里使用的同步的方式执行cmd指令
    public <T> T executeSync(SyncCommandCallback<T> callback) {
        //这里利用try的语法糖，执行完，自动给释放连接
        try (StatefulRedisConnection<String, String> connection = lettucePoolConfig.getRedisConnectionPool().borrowObject()) {
            //开启自动提交，如果false，命令会被缓冲，调用flushCommand()方法发出
            connection.setAutoFlushCommands(true);
            //设置为同步模式
            RedisCommands<String, String> commands = connection.sync();
            //执行传入的实现类
            return callback.doInConnection(commands);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //分装一个set方法
    public String set(final String key, final String val) {
        return executeSync(commands -> commands.set(key, val));
    }

    //分装一个get方法
    public String get(final String key) {
        return executeSync(commands -> commands.get(key));
    }

}
