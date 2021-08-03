package com.example.springboot.source.code.analysis.event;

/**
 * Created by ipipman on 2021/8/3.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.event
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/8/3 2:58 下午
 */

// 天气监听器
public interface WeatherListener {

    // 监听天气事件
    void onWeatherEvent(WeatherEvent event);
}
