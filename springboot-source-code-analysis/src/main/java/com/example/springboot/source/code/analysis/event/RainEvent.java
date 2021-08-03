package com.example.springboot.source.code.analysis.event;

/**
 * Created by ipipman on 2021/8/3.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.event
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/8/3 2:52 下午
 */

// 下雨事件
public class RainEvent extends WeatherEvent {

    @Override
    public String getWeather() {
        return "rain";
    }
}
