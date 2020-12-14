package com.ipman.rpc.thrift.provide.springboot.server;

import com.ipman.rpc.thrift.provide.springboot.dto.DemoServiceDTO;
import com.ipman.rpc.thrift.provide.springboot.impl.DemoServiceImpl;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by ipipman on 2020/12/14.
 *
 * @version V1.0
 * @Package com.ipman.rpc.thrift.provide.springboot.server
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/14 4:54 下午
 */
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
