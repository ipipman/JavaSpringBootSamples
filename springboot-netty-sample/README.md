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







