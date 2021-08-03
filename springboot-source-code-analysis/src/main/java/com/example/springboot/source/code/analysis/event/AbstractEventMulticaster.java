package com.example.springboot.source.code.analysis.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ipipman on 2021/8/3.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.event
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/8/3 3:20 下午
 */


// 抽象的事件广播器
public abstract class AbstractEventMulticaster implements EventMulticaster {

    // 事件监听器列表
    private List<WeatherListener> listenerList = new CopyOnWriteArrayList<>();

    @Override
    public void multicastEvent(WeatherEvent event) {
        // 广播所有事件监听器
        listenerList.forEach(listener -> {
            doStart();
            // 触发
            listener.onWeatherEvent(event);
            doEnd();
        });
    }

    // 添加事件监听器
    @Override
    public void addListener(WeatherListener listener) {
        listenerList.add(listener);
    }

    // 删除事件监听器
    @Override
    public void removeListener(WeatherListener listener) {
        listenerList.remove(listener);
    }


    // 模版方法
    abstract void doStart();

    abstract void doEnd();
}
