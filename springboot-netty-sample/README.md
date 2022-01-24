### Netty 笔记

#### 1.Netty对三种I/O模式的支持

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gyol111yspj31aa0jggob.jpg" alt="image-20220124110420220" width="700" align="left" />

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



#### 2.Netty NIO 中的Reactor 开发模式

Netty 三种开发模式版本

BIO 下是 Thread-Per-Connection

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gyolp1ody6j31fy0kggpn.jpg" alt="image-20220124112504631" width="700" align="left"/>

***Thread-Per-Connection：对应每个连接都有1个线程处理，1个线程同时处理：读取、解码、计算、编码、发送***



NIO 下是 Reactor

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gyolp5zqpaj316w0pmadn.jpg" alt="image-20220124112753682" width="700"  align="left" >

***Reactor 多线程模式，由多个线程负责：读取、发送，由线程池负责处理：解码、计算、编码*** 

***Reactor 主从多线程模式，由单独mainReactor 单线程负责接收请求，subReactor和 Reactor 多线程模式一致***



AIO 下是 Proactor

Reactor 是一种开发模式，模式的核心流程：

> 注册感兴趣的事件 -> 扫描是否有感兴趣的事件发生 -> 事件发生后做出相应的处理



##### Netty下使用 NIO 示范例

-  Reactor 单线程模式

```java
//线程数1
EventLoopGroup eventGroup = new NioEventLoopGroup(1);

ServerBootStrap serverBootStrap = new ServerBootStrap();
serverBootStrap.group(eventGroup);
```

- Reactor 多线程模式

```java
//多线程，不传具体的线程数时，Netty会根据CPU核心数分配
EventLoopGroup eventGroup = new NioEventLoopGroup();

ServerBootStrap serverBootStrap = new ServerBootStrap();
serverBootStrap.group(serverBootStrap);
```

- Reactor 主从多线程模式

```java
// 主线程负责接收请求 acceptor，是单线程
EventLoopGroup bossGroup = new NioEventLoopGroup();
// 从线程负责：读取、解码、计算、编码、发送，是多线程
EventLoopGroup workerGroup = new NioEventLoopGroup();

SeverBootStrap serverBootStrap = new ServerBootStrap();
serverBootStrap.group(bossGroup, workerGroup);
```



#### 3.Netty 粘包/半包解决方案

关于半包的主要原因：

- 发送写入数据 > 套接字缓冲区大小
- 发送的数据大于协议 MTU（Maximum Transmission Unit，最大传输单元），必须拆包

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gyorpiywgrj30z00aq0ul.jpg" alt="image-20220124144456949" width="700" align="left" />



关于粘包的主要原因：

- 发送方每次写入数据 > 套接字缓冲区大小
- 接收方读取套接字缓冲区不够及时



换个角度看原因：

- 收发时：一个发送可能多次接收，多个发送可能被一次接收
- 传输时：一个发送可能占用多个传输包，多个发送可能公用一个传输包



导致粘包/半包的根本原因：

TCP 是流式协议，消息无边界

> 提醒：UDP 像邮寄的包裹，虽然一次运输多个，但每个包裹都是 “界限”，一个一个签收，所以无粘包、半包问题



***解决粘包和半包的手段： 找出消息的边界***

- 方式一：***短连接***（不推荐）
  - 手段：TCP 连接改成短连接，一个请求一个短连接，建立连接到释放连接之间的信息即为传输信息
  - 优点：简单
  - 缺点：效率低下

- 方式二：***固定长度***（不推荐）
  - 手段：满足固定长度即可
  - 优点：简单
  - 缺点：浪费空间

- 方式三：***分隔符***（推荐）
  - 手段：用确定分隔符切割
  - 优点：空间不浪费，也比较简单
  - 缺点：内容本身出现分隔符时需要转义，所以需要扫描内容

- 方式四：***固定长度字段存个内容的长度信息***（推荐+）
  - 手段：先解析固定长度的字段获取长度，然后读取后续的内容
  - 优点：精确定位用户数据，内容也不用转义
  - 缺点：长度理论上是有限制，需要提前预知可能的最大长度从而定义长度占用字节数

- 方式五：***序列化方式***（根据场景衡量）
  - 手段：每种都不同，例如JSON 可以看{} 是否应己成对
  - 优缺点：衡量实际场景，很多是对现有协议的支持



##### Netty 粘包/半包 解决方案

- 固定长度 
  - 解码：FixedLengthFrameDecoder
- 分隔符
  - 解码：DelimiterBasedFrameDecoder
- 固定长度存个内容长度字段
  - 解码：LengthFieldBasedFrameDecoder
  - 编码：LengthFieldPerpender



