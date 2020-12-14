package com.ipman.rpc.thrift.provide.springboot.impl;

import com.ipman.rpc.thrift.provide.springboot.dto.DemoServiceDTO;
import com.ipman.rpc.thrift.provide.springboot.pojo.DemoPOJO;
import com.ipman.rpc.thrift.provide.springboot.pojo.ExceptionPOJO;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

/**
 * Created by ipipman on 2020/12/14.
 *
 * @version V1.0
 * @Package com.ipman.rpc.thrift.provide.springboot.impl
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/14 4:51 下午
 */
@Service
public class DemoServiceImpl implements DemoServiceDTO.Iface {

    @Override
    public DemoPOJO getStudentByName(String name) throws ExceptionPOJO, TException {
        DemoPOJO demoPOJO = new DemoPOJO();
        demoPOJO.setName("ipman");
        System.out.println("Thrift-Server 返回成功");
        return demoPOJO;
    }

    @Override
    public void save(DemoPOJO demo) throws ExceptionPOJO, TException {
        System.out.println("Thrift-Server 保存成功");
    }
}
