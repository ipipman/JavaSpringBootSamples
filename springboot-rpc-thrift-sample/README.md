# RPC服务框架之Thrift

### 一.什么是Thrift？
##### Thrift是跨语言的RPC框架，提供多语言编译功能，并提供了多种服务器工作模式； 
##### 用户通过Thrift的IDL（接口定义语言）来描述接口函数及数据类型的定义，然后通过Thrift生成各种语言的接口文件进行开发；

##### Thrift支持多种传输协议格式
> - TbinaryProtocol 二进制格式；
>
> - TCompactProtocol 压缩格式；
>
> - TJSONProtocol JSON格式；
>
> - TSimpleJSONProtocol 提供JSON只写协议，生成文件很容易通过脚本语言解析；
>
> - TDebugProtocol 可读的文件格式，以便于debug

##### Thrift支持多种数据传输方式
> - TSokect - 阻塞式Sokect；
>
> - TFramedTransport  已Frame为单位进行传输，非阻塞式服务中使用；
>
> - TFileTransport 以文件的方式进行传输；
>
> - TMemoryTransport 将内存用于I/O的方式进行传输；

##### Thrift支持多种的服务模型
> - TSimpleServer 单线程模型，一般用于调试；
>
> - ThreadPoolServer 多线程服务模式，使用标准的阻塞IO；
>
> - TNonblockingServer 多线程服务模型，使用非阻塞式IO（需要使用TFramedTransport传输方式）;

##### Thrift服务端最佳选择
> - 传输协议格式使用：TcompactProtocol;
>
> - 数据传输方式使用：TFramedTransport;
>
> - 服务模型：TNonblockingServer;

### 二、如何使用Thrift？
####  1.在使用Thrift框架前，需要先针对业务设计IDL描述文件，然后生成对应的Java接口文件后完成对应的接口实现类。
##### IDL语法参考：[http://zanphpdoc.zanphp.io/nova/IDL_syntax.html](http://zanphpdoc.zanphp.io/nova/IDL_syntax.html "http://zanphpdoc.zanphp.io/nova/IDL_syntax.html")
```java
//定义代码生成后存放的位置
namespace java com.ipman.rpc.thrift.provide.springboot.pojo

//将shrift的数据类型格式转换为java习惯的格式
typedef i16 short
typedef i32 int
typedef i64 long
typedef string String
typedef bool boolean

//定义demo对象
struct DemoPOJO {
    1:optional String name
}

//定义数据异常
exception ExceptionPOJO {
    //optional 可选 非必传
    1:optional int code,
    2:optional String msg
}

//定义操作demo服务
service DemoServiceDTO {
    //根据名称返回一个demo，required 必传项
    DemoPOJO getStudentByName(1:required String name) throws (1:ExceptionPOJO dataException),

    //保存一个demo信息 无返回 抛出DataException异常
    void save(1:required DemoPOJO demo) throws (1:ExceptionPOJO dataException)
}

```
####  2. 生成JAVA接口文件
> - /usr/local/opt/thrift@0.9/bin/thrift --gen java  demo.thrift

####  3.编写Provide端Server代码
##### 需要注意的是服务端与客户端的传输协议格式和传输服务模型必须一致！

```java
@Component
public class ThriftServer {

    //监听的端口
    @Value("${server.thrift.port}")
    private Integer port;

    //线程池最小线程数
    @Value("${server.thrift.min-thread-pool}")
    private Integer minThreadPool;

    //线程池最大线程数
    @Value("${server.thrift.max-thread-pool}")
    private Integer maxThreadPool;

    @Resource
    DemoServiceImpl demoService;

    public void start() {
        try {
            //设置Thrift的为非阻塞的Sokect
            TNonblockingServerSocket socket = new TNonblockingServerSocket(port);

            //设置Server
            THsHaServer.Args args = new THsHaServer.Args(socket)
                    .minWorkerThreads(minThreadPool)
                    .maxWorkerThreads(maxThreadPool);

            //Thrift传输协议
            //1. TBinaryProtocol      二进制传输协议
            //2. TCompactProtocol     压缩协议 他是基于TBinaryProtocol二进制协议在进一步的压缩，使得体积更小
            //3. TJSONProtocol        Json格式传输协议
            //4. TSimpleJSONProtocol  简单JSON只写协议，生成的文件很容易通过脚本语言解析，实际开发中很少使用
            //5. TDebugProtocol       简单易懂的可读协议，调试的时候用于方便追踪传输过程中的数据
            args.protocolFactory(new TCompactProtocol.Factory());

            //Thrift数据传输方式
            //1. TSocker            阻塞式Scoker 相当于Java中的ServerSocket
            //2. TFrameTransport    以frame为单位进行数据传输，非阻塞式服务中使用
            //3. TFileTransport     以文件的形式进行传输
            //4. TMemoryTransport   将内存用于IO,Java实现的时候内部实际上是使用了简单的ByteArrayOutputStream
            //5. TZlibTransport     使用zlib进行压缩，与其他传世方式联合使用；java当前无实现所以无法使用
            args.transportFactory(new TFramedTransport.Factory());

            //添加业务
            DemoServiceDTO.Processor<DemoServiceImpl> processor = new DemoServiceDTO.Processor<>(demoService);
            args.processorFactory(new TProcessorFactory(processor));

            //thrift支持的服务模型
            //1.TSimpleServer  简单的单线程服务模型，用于测试
            //2.TThreadPoolServer 多线程服务模型，使用的标准的阻塞式IO;运用了线程池，当线程池不够时会创建新的线程,当线程池出现大量空闲线程，线程池会对线程进行回收
            //3.TNonBlockingServer 多线程服务模型，使用非阻塞式IO（需要使用TFramedTransport数据传输方式）
            //4.THsHaServer YHsHa引入了线程池去处理（需要使用TFramedTransport数据传输方式），其模型把读写任务放到线程池去处理;Half-sync/Half-async（半同步半异步）的处理模式;Half-sync是在处理IO时间上（sccept/read/writr io）,Half-async用于handler对RPC的同步处理
            TServer server = new THsHaServer(args);

            System.out.println("Thrift Server 端口：" + port + "， 启动成功");
            //启动server
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
```

####  4.编写Consume端Server代码
##### 需要注意的是服务端与客户端的传输协议格式和传输服务模型必须一致！
```java
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
```






