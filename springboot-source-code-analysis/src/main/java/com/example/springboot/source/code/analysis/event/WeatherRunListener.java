package com.example.springboot.source.code.analysis.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ipipman on 2021/8/9.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.event
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/8/9 3:46 下午
 */

@Component
public class WeatherRunListener {

    @Autowired
    private WeatherEventMulticaster weatherEventMulticaster;

    // 下雪事件
    public void snow(){
        weatherEventMulticaster.multicastEvent(new SnowEvent());
    }

    // 下雨事件
    public void rain(){
        weatherEventMulticaster.multicastEvent(new RainEvent());
    }

    // 天津监听器
    public void addListener(WeatherListener listener){
        weatherEventMulticaster.addListener(listener);
    }
}
