package com.ipman.dubbo.xml.provider;

import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:provider/dubbo-hello-provider.xml")
public class DubboXmlProviderApplication {

    private static TestingServer server;

    @SneakyThrows
    public static void main(String[] args) {
        //启动ZK
        mockZKServer();

        SpringApplication.run(DubboXmlProviderApplication.class, args);
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
