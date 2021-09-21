package com.example.springboot.source.code.analysis;

import com.example.springboot.source.code.analysis.event.RainListener;
import com.example.springboot.source.code.analysis.event.WeatherRunListener;
import com.example.springboot.source.code.analysis.listener.ApplicationContextContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = SpringbootSourceCodeAnalysisApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
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


    @Test
    public void contextLoads() {
    }


}
