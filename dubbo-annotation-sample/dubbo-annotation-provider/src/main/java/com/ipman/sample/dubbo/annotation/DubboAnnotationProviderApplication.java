package com.ipman.sample.dubbo.annotation;

import com.ipman.sample.dubbo.annotation.config.ProviderConfiguration;
import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class DubboAnnotationProviderApplication {

    private static TestingServer server;

    @SneakyThrows
    public static void main(String[] args) {
        //开启ZK
        DubboAnnotationProviderApplication.mockZKServer();

        Thread.sleep(3000);

        //开启Dubbo
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProviderConfiguration.class);
        context.start();
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
