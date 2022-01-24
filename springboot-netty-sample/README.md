### Netty 笔记

#### Netty对三种I/O模式的支持

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gyol111yspj31aa0jggob.jpg" alt="image-20220124110420220" style="zoom:50%;" align="left" />

##### Netty并不是只支持过NIO，但是不建议（depercate）阻塞I/O（BIO/OIO）

- 连接数高的情况下：阻塞 -> 消耗源、效率低

##### Netty也不建议（depercate）使用AIO

- AIO在Windows 下比较成熟，但是很少用来做服务器
- Linux 常用来做服务器，但是AIO实现不够成熟
- Linux 的AIO相比NIO的性能没有显著的提升，反而会为开发者带来高额的维护成本

##### Netty和JDK NIO在Linux下，都是基于epoll实现，为什么要用Netty？

-  Netty 暴露了更多的可用参数，如：
  - JDK 的 NIO 默认实现是水平触发
  - Netty 是边缘触发（默认）和水平触发可以切换
- Netty 实现的垃圾回收更少、性能更好



#### Netty NIO 中的Reactor 开发模式

Netty 三种开发模式版本

- BIO 下是 Thread-Per-Connection

​	<img src="https://tva1.sinaimg.cn/large/008i3skNly1gyolp1ody6j31fy0kggpn.jpg" alt="image-20220124112504631" style="zoom:40%;" algin="left"/>

***Thread-Per-Connection：对应每个连接都有1个线程处理，1个线程同时处理：读取、解码、计算、编码、发送***



- NIO 下是 Reactor

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gyolp5zqpaj316w0pmadn.jpg" alt="image-20220124112753682" style="zoom:40%;"  align="left" >

***Reactor 多线程模式，由多个线程负责：读取、发送，由线程池负责处理：解码、计算、编码*** 

***Reactor 主从多线程模式，由单独mainReactor 单线程负责接收请求，subReactor和 Reactor 多线程模式一致***



- AIO 下是 Proactor

Reactor 是一种开发模式，模式的核心流程：

> 注册感兴趣的事件 -> 扫描是否有感兴趣的事件发生 -> 事件发生后做出相应的处理



##### Netty下使用 NIO 示范例

-  Reactor 单线程模式

  - ```java
    //线程数1
    EventLoopGroup eventGroup = new NioEventLoopGroup(1);
    
    ServerBootStrap serverBootStrap = new ServerBootStrap();
    serverBootStrap.group(eventGroup);
    ```

- Reactor 多线程模式

  - ```java
    //多线程，不传具体的线程数时，Netty会根据CPU核心数分配
    EventLoopGroup eventGroup = new NioEventLoopGroup();
    
    ServerBootStrap serverBootStrap = new ServerBootStrap();
    serverBootStrap.group(serverBootStrap);
    ```

- Reactor 主从多线程模式

  - ```java
    // 主线程负责接收请求 acceptor，是单线程
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    // 从线程负责：读取、解码、计算、编码、发送，是多线程
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    
    SeverBootStrap serverBootStrap = new ServerBootStrap();
    serverBootStrap.group(bossGroup, workerGroup);
    ```

  

  





