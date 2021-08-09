package com.example.springboot.source.code.analysis.event;

/**
 * Created by ipipman on 2021/8/3.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.event
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/8/3 2:50 下午
 */


// 天气事件抽象类
public abstract class WeatherEvent {

    // 获取当前天气的事件
    public abstract String getWeather();

}
