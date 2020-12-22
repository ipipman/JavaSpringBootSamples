package com.ipman.dubbo.notify.sample.api;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.notify.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 10:14 上午
 */
public interface Notify {

    void onReturn(String name, int id);

    void onThrow(Throwable ex, int id);
}
