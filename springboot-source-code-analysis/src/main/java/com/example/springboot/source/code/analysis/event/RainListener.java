package com.example.springboot.source.code.analysis.event;

import org.springframework.stereotype.Component;

/**
 * Created by ipipman on 2021/8/3.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.event
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/8/3 3:00 下午
 */


// 下雨监听器
@Component
public class RainListener implements WeatherListener {

    @Override
    public void onWeatherEvent(WeatherEvent event) {
        if (event instanceof RainEvent) {
            
            System.out.println("rain event ..." + event.getWeather());
        }
    }
}
