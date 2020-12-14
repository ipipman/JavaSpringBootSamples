package com.ipman.rpc.thrift.consume.springboot.client;

import com.ipman.rpc.thrift.consume.springboot.dto.DemoServiceDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by ipipman on 2020/12/14.
 *
 * @version V1.0
 * @Package com.ipman.rpc.thrift.consume.springboot.client
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/14 5:41 下午
 */
public class ThriftClient {

    @Setter
    private String host;

    @Setter
    private Integer port;

    private TTransport tTransport;

    private TProtocol tProtocol;

    private DemoServiceDTO.Client client;

    private void init(){
        tTransport = new TFramedTransport(new TSocket(host, port), 600);
        //协议对象，需要和服务端保持一致
        tProtocol = new TCompactProtocol(tTransport);
        client = new DemoServiceDTO.Client(tProtocol);
    }

    public DemoServiceDTO.Client getClient() {
        return client;
    }

}
