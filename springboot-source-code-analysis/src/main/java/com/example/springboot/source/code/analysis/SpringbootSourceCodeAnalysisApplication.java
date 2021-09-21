package com.example.springboot.source.code.analysis;

import com.example.springboot.source.code.analysis.initializer.SecondInitializer;
import com.example.springboot.source.code.analysis.listener.SecondListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootSourceCodeAnalysisApplication {

    public static void main(String[] args) {
        // SpringApplication.run(SpringbootSourceCodeAnalysisApplication.class, args);
        SpringApplication springApplication = new SpringApplication(SpringbootSourceCodeAnalysisApplication.class);

        // 手动添加一个框架的初始化器
        springApplication.addInitializers(new SecondInitializer());

        // 手动添加一个框架事件监听器
        springApplication.addListeners(new SecondListener());

        springApplication.run();

    }
}
