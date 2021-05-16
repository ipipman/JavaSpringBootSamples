package com.ipman.dubbo.spi.sample.spi;

import com.alibaba.fastjson.JSON;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance;

import java.util.List;

/**
 * Created by ipipman on 2020/12/23.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.spi.sample.spi
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/23 10:55 上午
 */
public class CustomConsumerLoadBalance extends RandomLoadBalance implements LoadBalance {

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException{
        for (Invoker<T> t : invokers) {
            try {
                URL u = t.getUrl();
                System.out.println("[ CustomConsumerLoadBalance doSelect ] select=" + invocation.getServiceName()
                        + "，method=" + invocation.getMethodName() + "，args=" + JSON.toJSONString(invocation.getArguments())
                        + "，doSelect=" + u.getAddress() + ":" + u.getPort());
                return t;
            } catch (Exception e) {
                // throw e
            }
        }
        return null;
    }

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        return super.doSelect(invokers, url, invocation);
    }
}
