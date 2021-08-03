package com.example.springboot.source.code.analysis.event;

/**
 * Created by ipipman on 2021/8/3.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.event
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/8/3 3:29 下午
 */


public class Test {

    public static void main(String[] args) {
        // 创建事件广播器
        WeatherEventMulticaster multicaster = new WeatherEventMulticaster();

        // 添加事件监听器
        WeatherListener listener1 = new SnowListener();
        WeatherListener listener2 = new RainListener();
        multicaster.addListener(listener1);
        multicaster.addListener(listener2);

        // 广播事件
        WeatherEvent event1 = new RainEvent();
        WeatherEvent event2 = new SnowEvent();
        multicaster.multicastEvent(event1);
        multicaster.multicastEvent(event2);
    }

}
