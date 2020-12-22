package com.ipman.dubbo.notify.sample.impl;

import com.ipman.dubbo.notify.sample.api.Notify;

/**
 * Created by ipipman on 2020/12/22.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.notify.sample.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/22 10:17 上午
 */
public class NotifyImpl implements Notify {

    @Override
    public void onReturn(String name, int id) {
        System.out.println("onReturn：name=" + name + "，id=" + id);
    }

    @Override
    public void onThrow(Throwable ex, int id) {
        System.out.println("onThrow：ex=" + ex + "，id=" + id);
    }
}
