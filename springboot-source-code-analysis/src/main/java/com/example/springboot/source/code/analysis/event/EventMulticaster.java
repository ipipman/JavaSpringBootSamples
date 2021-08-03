package com.example.springboot.source.code.analysis.event;

/**
 * Created by ipipman on 2021/8/3.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.event
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/8/3 3:04 下午
 */

// 事件广播器
public interface EventMulticaster {

    // 广播事件
    void multicastEvent(WeatherEvent event);

    // 添加监听器
    void addListener(WeatherListener listener);

    // 删除监听器
    void removeListener(WeatherListener listener);
}
