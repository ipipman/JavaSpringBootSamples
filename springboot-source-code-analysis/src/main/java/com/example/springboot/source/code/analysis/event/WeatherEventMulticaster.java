package com.example.springboot.source.code.analysis.event;

/**
 * Created by ipipman on 2021/8/3.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.event
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/8/3 3:27 下午
 */

// 天气事件广播器
public class WeatherEventMulticaster extends AbstractEventMulticaster {

    @Override
    public void doStart() {
        System.out.println("multicaster event begin ...");
    }

    @Override
    public void doEnd() {
        System.out.println("multicaster evnet end ....");
    }
}
