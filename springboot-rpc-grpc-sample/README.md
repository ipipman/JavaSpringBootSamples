# RPC服务框架之gRPC

### 一.什么是RPC？
##### RPC原理简图
<img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/grpc/831608122149_.pic_hd.jpg" width = "600" height = "340" alt="图片名称" align=center />

### 二.什么是gRPC？
##### gRPC是一个高性能、通用的开源RPC框架，基于HTTP/2二进制协议标准而设计，基于ProtoBuf（Protocol Buffers）序列化协议开发，且支持众多开发语言。

##### ProtoBuf具有了优秀的系列化协议所需的众多典型特性
ProtoBuf是一个存粹的展示层协议，可以和各种传输层协议一起使用
> - 具有标准的IDL和IDL编译器；
> - 序列化数据非常简介，紧凑，与XML相比，其序列化之后的数据量约为1/3和1/10；
> - 解析速度非常快，比对应的XML快20～100倍速度


#### gRPC服务端创建流程
<img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/grpc/841608122163_.pic_hd.jpg" width = "660" height = "395" alt="图片名称" align=center />

> - gRPC服务端基于NettyServer负责监听Socket地址，基于HTTP2协议的写入;
> - gPRC服务接口并不是使用反射来实现的，而是通过Proto工具生成的代码，将服务接口注册到gRPC内部的服务上，性能较高；
> - ServerImpl负责整个gRPC服务端消息的调度和处理，会初始化：Netty线程池、gRPC的线程池、内部服务注册等；

#### gRPC消息接入流程
<img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/grpc/851608122185_.pic_hd.jpg" width = "700" height = "420" alt="图片名称" align=center />

> - gRPC消息由netty/http2协议负责接入，通过gRPC注册的Http2FrameLister将解码后的Http Header和Http Body后，发送到gRPC的NettyServerHandler，实现Netty Http/2的消息接入；


### 三.gRPC实战
##### 1.编写PB文件，proto语法参考：[https://developers.google.com/protocol-buffers/docs/proto](https://developers.google.com/protocol-buffers/docs/proto "https://developers.google.com/protocol-buffers/docs/proto")
```java
syntax = "proto3";

option java_package = "com.ipman.rpc.grpc.springboot.lib";

// The greeter service definition.
service Greeter {
    // Sends a greeting
    rpc SayHello ( HelloRequest) returns (  HelloReply) {}

}
// The request message containing the user's name.
message HelloRequest {
    string name = 1;
}
// The response message containing the greetings
message HelloReply {
    string message = 1;
}
```

##### 2.编写Server端，通过@GrpcService注解注册gRPC服务
```java
@Slf4j
@GrpcService(GreeterOuterClass.class)
public class GreeterService extends GreeterGrpc.GreeterImplBase {

    /**
     * gRPC Server started, listening on address: 0.0.0.0, port: 8800
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void sayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        String message = "Hello " + request.getName();
        final GreeterOuterClass.HelloReply.Builder replyBuilder = GreeterOuterClass.HelloReply.newBuilder().setMessage(message);
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
        log.info("Returning " + message);
    }
}
```

##### 3.编写Client端，通过@GrpcClient绑定gRPC服务端地址与端口等信息
```java
@Service
public class GrpcClientServiceImpl implements IGrpcClientService {

    @GrpcClient("local-grpc-server")
    private Channel serverChannel;

    /**
     * 通过本地存protocol buffer存根序列化后调用gRPC服务端
     *
     * @param name
     * @return
     */
    @Override
    public String sendMessage(String name) {
        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(serverChannel);
        GreeterOuterClass.HelloReply response = stub.sayHello(GreeterOuterClass.HelloRequest.newBuilder().setName(name).build());
        return response.getMessage();
    }
}
```
