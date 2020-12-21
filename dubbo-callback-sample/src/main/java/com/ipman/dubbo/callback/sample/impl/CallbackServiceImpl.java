package com.ipman.dubbo.callback.sample.impl;

import com.ipman.dubbo.callback.sample.api.CallbackListener;
import com.ipman.dubbo.callback.sample.api.CallbackService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.callback.sample.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 9:17 下午
 */
public class CallbackServiceImpl implements CallbackService {

    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    public CallbackServiceImpl() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
                            entry.getValue().changed(getChanged(entry.getKey()));
                        } catch (Throwable t1) {
                            t1.printStackTrace();
                            listeners.remove(entry.getKey());
                        }
                    }
                    Thread.sleep(5000);
                } catch (Throwable t1) {
                    t1.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
        listener.changed(getChanged(key));
    }

    private String getChanged(String key) {
        return "Changed: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
