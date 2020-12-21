package com.ipman.dubbo.callback.sample.api;

/**
 * Created by ipipman on 2020/12/21.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.callback.sample.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/21 9:16 下午
 */
public interface CallbackService {

    void addListener(String key, CallbackListener listener);
}
