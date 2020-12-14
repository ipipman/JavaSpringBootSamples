package com.ipman.rpc.thrift.provide.springboot;

import com.ipman.rpc.thrift.provide.springboot.server.ThriftServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootRpcThriftProvideApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRpcThriftProvideApplication.class, args);
    }

    /**
     * 启动Thrift服务
     *
     * @return
     */
    @Bean(initMethod = "start")
    public ThriftServer init() {
        return new ThriftServer();
    }
}
