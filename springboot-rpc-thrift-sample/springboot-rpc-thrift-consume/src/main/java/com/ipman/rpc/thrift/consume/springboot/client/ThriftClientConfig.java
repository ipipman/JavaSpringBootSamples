package com.ipman.rpc.thrift.consume.springboot.client;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by ipipman on 2020/12/14.
 *
 * @version V1.0
 * @Package com.ipman.rpc.thrift.consume.springboot.client
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/14 5:38 下午
 */
@Configuration
public class ThriftClientConfig {

    //服务端的地址
    @Value("${server.thrift.host}")
    private String host;

    //服务端的端口
    @Value("${server.thrift.port}")
    private Integer port;

//    //初始化Bean的时候调用对象里面的init方法
//    @Bean(initMethod = "init")
//    //每次请求实例话一个新的ThriftClient连接对象
//    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public ThriftClient init() {
//        ThriftClient client = new ThriftClient();
//        client.setHost(host);
//        client.setPort(port);
//        return client;
//    }

    @Bean
    public ThriftClientConnectPoolFactory thriftPool() {
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        //创建一个池工厂对象, 交由Spring管理
        return new ThriftClientConnectPoolFactory(config, host, port);
    }
}
