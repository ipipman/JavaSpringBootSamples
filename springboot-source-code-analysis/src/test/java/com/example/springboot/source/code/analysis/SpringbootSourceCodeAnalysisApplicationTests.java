package com.example.springboot.source.code.analysis;

import com.example.springboot.source.code.analysis.event.RainListener;
import com.example.springboot.source.code.analysis.event.WeatherRunListener;
import com.example.springboot.source.code.analysis.ioc.ann.MyImportBeanDefinitionRegistrar;
import com.example.springboot.source.code.analysis.ioc.service.HelloService;
import com.example.springboot.source.code.analysis.listener.ApplicationContextContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = SpringbootSourceCodeAnalysisApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
// 通过XML注入Bean
//@ContextConfiguration(locations = "classpath:ioc/demo.xml")
// 通过ImportBeanDefinitionRegistrar进行Bean的注入
@Import(MyImportBeanDefinitionRegistrar.class)
public class SpringbootSourceCodeAnalysisApplicationTests {

    // 引入事件监听器
    @Autowired
    private WeatherRunListener weatherRunListener;

    // 测试事件监听器
    @Test
    public void testEvent() {
        weatherRunListener.addListener(new RainListener());
        weatherRunListener.snow();
        weatherRunListener.rain();
    }

    // 测试ContextRefreshEvent事件
    @Test
    public void testContextRefreshEvent() {
        WeatherRunListener weatherRunListener = ApplicationContextContainer.getInstance().getBean(WeatherRunListener.class);
        weatherRunListener.snow();
        weatherRunListener.rain();
    }


    @Autowired
    HelloService helloService;

    @Test
    public void testHello() {
        System.out.println(helloService.hello());
    }


    @Test
    public void contextLoads() {
    }


}
