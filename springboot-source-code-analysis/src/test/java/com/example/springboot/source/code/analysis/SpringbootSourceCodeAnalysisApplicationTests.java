package com.example.springboot.source.code.analysis;

import com.example.springboot.source.code.analysis.event.RainListener;
import com.example.springboot.source.code.analysis.event.WeatherRunListener;
import com.example.springboot.source.code.analysis.listener.ApplicationContextContainer;
import com.example.springboot.source.code.analysis.xml.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = SpringbootSourceCodeAnalysisApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:ioc/demo.xml")
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

    //  引入XML Bean
    @Autowired
    private HelloService helloService;

    @Test
    public void testHello(){
        System.out.println(helloService.hello());
    }


    @Test
    public void contextLoads() {
    }


}
