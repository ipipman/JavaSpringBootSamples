package com.ipman.rpc.grpc.springboot.service;

/**
 * Created by ipipman on 2020/12/15.
 *
 * @version V1.0
 * @Package com.ipman.rpc.grpc.springboot.service
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/15 6:28 下午
 */
public interface IGrpcClientService {
    String sendMessage(String name);
}
