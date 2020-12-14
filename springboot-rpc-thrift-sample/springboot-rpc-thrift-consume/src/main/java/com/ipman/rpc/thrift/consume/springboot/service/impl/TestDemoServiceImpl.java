package com.ipman.rpc.thrift.consume.springboot.service.impl;

import com.ipman.rpc.thrift.consume.springboot.client.TTSocket;
import com.ipman.rpc.thrift.consume.springboot.client.ThriftClientConnectPoolFactory;
import com.ipman.rpc.thrift.consume.springboot.pojo.DemoPOJO;
import com.ipman.rpc.thrift.consume.springboot.service.TestDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ipipman on 2020/12/14.
 *
 * @version V1.0
 * @Package com.ipman.rpc.thrift.consume.springboot.service.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/14 5:33 下午
 */
@Service
public class TestDemoServiceImpl implements TestDemoService {

    @Autowired
    ThriftClientConnectPoolFactory thriftPool;

    //根据名称获取Demo
    @Override
    public DemoPOJO getDemoItem(String name) {
        TTSocket ttSocket = null;
        try {
            //通过对象池获得一个客户端链接
            ttSocket = thriftPool.getConnect();
            return ttSocket.getService().getStudentByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常从连接器移出
            thriftPool.invalidateObject(ttSocket);
            ttSocket = null;
        } finally {
            if (ttSocket != null) {
                //归还链接
                thriftPool.returnConnection(ttSocket);
            }
        }
        return null;
    }

    //保存Demo信息
    @Override
    public void save(DemoPOJO demoPOJO) {
        TTSocket ttSocket = null;
        try {
            //通过对象池获得一个客户端链接
            ttSocket = thriftPool.getConnect();
            ttSocket.getService().save(demoPOJO);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常从连接器移出
            thriftPool.invalidateObject(ttSocket);
            ttSocket = null;
        } finally {
            if (ttSocket != null) {
                //归还链接
                thriftPool.returnConnection(ttSocket);
            }
        }
    }
}
