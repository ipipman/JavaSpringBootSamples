package com.ipman.sharding.jdbc.springboot;

import com.ipman.sharding.jdbc.springboot.service.impl.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringbootShardingsphereJdbcSampleApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringbootShardingsphereJdbcSampleApplication.class, args);

        DemoService demoService = (DemoService) context.getBean("DemoService");
        //测试读写分离
        demoService.testMasterSlave();
        //测试读写分离事务
        demoService.testTransaction();
    }

}
