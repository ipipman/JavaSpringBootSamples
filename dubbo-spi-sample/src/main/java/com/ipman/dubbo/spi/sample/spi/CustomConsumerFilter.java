package com.ipman.dubbo.spi.sample.spi;

import com.alibaba.fastjson.JSON;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * Created by ipipman on 2020/12/23.
 *
 * @version V1.0
 * @Package com.ipman.dubbo.spi.sample.spi
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/23 10:20 上午
 */
@Activate(group = CommonConstants.CONSUMER) //用于表示当前SPI扩展只在客户端被激活
public class CustomConsumerFilter implements Filter, Filter.Listener {

    //[ CustomConsumerFilter invoke ]
    // service=com.ipman.dubbo.spi.sample.api.DemoService，
    // method=sayHello，
    // args=["ipman"]
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        System.out.println("[ CustomConsumerFilter invoke ] service=" + invocation.getServiceName()
                + "，method=" + invocation.getMethodName() + "，args=" + JSON.toJSONString(invocation.getArguments()));
        if (("sayHello").equals(invocation.getMethodName())) {
            //throw new RpcException("You do not have access to this method");
        }
        return invoker.invoke(invocation);
    }

    //[ CustomConsumerFilter onResponse ]
    // service=com.ipman.dubbo.spi.sample.api.DemoService，method=sayHello，
    // args=["ipman"]，
    // response="Hello ipman, response from provider: 10.13.224.253:20880"
    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        System.out.println("[ CustomConsumerFilter onResponse ] service=" + invocation.getServiceName()
                + "，method=" + invocation.getMethodName() + "，args=" + JSON.toJSONString(invocation.getArguments())
                + "，response=" + JSON.toJSONString(appResponse.getValue()));
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
        System.out.println("[ CustomConsumerFilter onError ] service=" + invocation.getServiceName()
                + "，method=" + invocation.getMethodName() + "，args=" + JSON.toJSONString(invocation.getArguments())
                + "，error=" + t.getMessage());
    }
}
