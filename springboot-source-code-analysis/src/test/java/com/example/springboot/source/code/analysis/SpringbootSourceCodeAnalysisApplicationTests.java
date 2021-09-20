package com.example.springboot.source.code.analysis;

import com.example.springboot.source.code.analysis.event.RainListener;
import com.example.springboot.source.code.analysis.event.WeatherRunListener;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootSourceCodeAnalysisApplicationTests {

    // 引入事件监听器
    @Autowired
    private WeatherRunListener weatherRunListener;

    // 测试事件监听器
    @Test
    public void testEvent(){
        weatherRunListener.addListener(new RainListener());
        weatherRunListener.snow();
        weatherRunListener.rain();
    }

    @Test
    void contextLoads() {
    }


}
