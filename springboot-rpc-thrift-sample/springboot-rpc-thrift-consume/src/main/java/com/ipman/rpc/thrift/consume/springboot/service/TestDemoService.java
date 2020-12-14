package com.ipman.rpc.thrift.consume.springboot.service;

import com.ipman.rpc.thrift.consume.springboot.pojo.DemoPOJO;

/**
 * Created by ipipman on 2020/12/14.
 *
 * @version V1.0
 * @Package com.ipman.rpc.thrift.consume.springboot.service
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/14 5:33 下午
 */
public interface TestDemoService {

    //根据名称获取Demo
    DemoPOJO getDemoItem(String name);

    //保存Demo信息
    void save(DemoPOJO demoPOJO);

}
