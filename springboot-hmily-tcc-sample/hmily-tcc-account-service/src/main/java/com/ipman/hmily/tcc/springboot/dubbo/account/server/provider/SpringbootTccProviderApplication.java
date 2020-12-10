package com.ipman.hmily.tcc.springboot.dubbo.account.server.provider;

import org.apache.curator.test.TestingServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:provider/*.xml")
@ComponentScan(basePackages = {"com.ipman.hmily.tcc.springboot.dubbo"})
@MapperScan("com.ipman.hmily.tcc.springboot.dubbo.account.mapper")
public class SpringbootTccProviderApplication {

    private static TestingServer server;

    public static void main(String[] args) throws Exception {
        //mock zk server
        mockZKServer();
        ApplicationContext applicationContext = SpringApplication.run(SpringbootTccProviderApplication.class, args);
    }

    /**
     * 启动本地ZK
     *
     * @throws Exception
     */
    private static void mockZKServer() throws Exception {
        //Mock zk server，作为  配置中心
        server = new TestingServer(2181, true);
        server.start();
    }

}
