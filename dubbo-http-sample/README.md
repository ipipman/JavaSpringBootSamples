# 分布式服务框架之Dubbo（基于HTTP传输协议）

### 什么是Dubbo？
#### Dubbo是一个分布式服务框架，致力于提供高性能和透明化的RPC远程服务调用方案，以及SOA服务治理方案。
分布式意味着，一个业务被拆分多个业务，部署在不同服务器上，既然各个服务部署在不同的服务器上，那服务间的调用就是通过网络通信的。既然涉及到了网络通信，那么服务消费者调用服务之前，都要写各种网络请求和编解码之类的相关代码，明显是很不友好的，Dubbo所说的透明化，就是指让调用者对网络请求和编解码的细节透明，让我们像调用本地方法一样去调用远程方法。

### Dubbo的架构
<img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/dubbo/991608375409_.pic.jpg" width = "500" height = "320" alt="图片名称" align=center />

#### 上述节点说明
> - Provider：暴露服务的服务提供方；
> - Consumer：调用远程服务的服务消费方；
> - Registry： 服务注册与发现的注册中心；
> - Monitor：统计服务的调用次数和调用时间的监控；
> - Container：服务运行容器；

#### 调用关系说明
1. 服务容器负责启动，加载，运行服务提供者。
2. 服务提供者在启动时，向注册中心注册自己提供的服务。
3. 服务消费者在启动时，向注册中心订阅自己所需的服务。
4. 注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。
5. 服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。
6. 服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。

#### Dubbo架构重要的知识点
> - 注册中心负责服务地址的注册和查找，相当于目录服务，服务提供者和消费者只在启动时与注册中心交互，注册中心不转发请求，压力较小；
> - 监控中心负责统计各个服务调用次数，调用时间等，统计先在内容汇总后每分钟一次发送到监控中心服务器，并以报表的方式展现；
> - 注册中心，服务提供者，服务消费者三者均为长连接，监控中心除外；
> - 注册中心通过长连接感知服务提供者的存在，服务提供者宕机，注册中心将立即推送事件通知消费者；
> - 注册中心和监控中心全部宕机，不影响已运行的提供者和消费者，消费者在本地缓存了提供者的列表；
> - 注册中心和监控中心都是可选的，消费者也可以直连服务提供者；
> - 服务提供者无状态，任意一台宕掉后，不影响使用；
> - 服务提供者全部宕掉后，服务消费者应用将无法使用，并无限次重连等待服务提供者恢复；

### Dubbo 工作原理
<img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/dubbo/1001608375467_.pic.jpg" width = "700" height = "640" alt="图片名称" align=center />

#### Dubbo各层说明，除了Service和Config层为API，其它各层均为SPI
> - 第一层：service层，接口层，给服务提供者和消费者来实现的；
> - 第二层：config层，配置层，主要是对dubbo进行各种配置的；
> - 第三层：proxy层，服务接口透明代理，生成服务的客户端Stub和服务端Skeleton；
> - 第四层：registry层，服务注册层，负责服务的注册和发现；
> - 第五层：cluster层，集群层，封装多个服务提供者的路由以及负载均衡，将多个实例组合成一个服务；
> - 第六层：monitor层，监控层，对rpc接口的次数和调用时间进行监控；
> - 第七层：protocol层，远程调用曾，分装rpc调用；
> - 第八层：exchange层，信息交换层，分装请求相应模式，同步转异步；
> - 第九层：transport层，网络传输层，抽象mina和netty为统一接口；
> - 第十层：serialize层，数据序列化层，网络传输需要；

#### Dubbo提供的负载均衡策略
在集群负载均衡时，Dubbo提供了多种负载均衡策略，默认是Random随机调用
1. Random LoadBalance
	- 随机，按权重设计随机概率；
	- 在一个截面上碰撞的机率高，但调用量越大分布越均匀；
2. RoundRobin LoadBalance
	- 轮询，按公约后的权重设置轮训比率；
	- 存在慢的提供者累积请求的问题，比如：第二台机器很慢，但是没挂，当请求调用第二台时就卡在那，久而久之，所有的请求都在调到第二台上；
3. LeastActive LoadBalance
	- 最少活跃调用数，相同活跃数的随机，活跃数指调用前后计数差；
	- 使慢的提供者收到更少的请求，因为越慢的提供者的调用前后计数差会越大；
4. ConsistentHash LoadBalance
	- 一致性Hash，相同参数的请求总是发到同一提供者

#### Dubbo的健壮性
1. 监控中心宕掉不影响使用，只是会丢失部分采集数据；
2. 注册中心对等集群，任意一台宕掉后，将自动切换到另一台；
3. 注册中心全部宕掉后，服务提供者和服务消费者仍然可以通过本地缓存列表进行通讯；
4. 服务提供者无状态，任意一台宕掉后，不影响使用；
5. 服务提供者全部宕机后，服务消费者应用将无法使用，并无限次数的重试等待服务提供者恢复；

### Dubbo HTTP协议实战
#### 1. 编写Provider端服务发布者XML配置，指定传输协议为HTTP
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:property-placeholder/>

    <dubbo:application name="http-provider"/>

    <dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:2181"/>

    <!--  配置http协议  -->
    <dubbo:protocol name="http" id="http" port="${servlet.port:8080}" server="${servlet.container:tomcat}"/>

    <bean id="demoServiceImpl" class="com.ipman.dubbo.http.sample.impl.DemoServiceImpl" />

    <dubbo:service interface="com.ipman.dubbo.http.sample.api.DemoService" ref="demoServiceImpl" protocol="http"/>
</beans>
```
#### 2. 编写Consumer端服务发布者XML配置，指定传输协议为HTTP
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:property-placeholder/>

    <dubbo:application name="http-consumer"/>

    <bean id="demoService" class="com.ipman.dubbo.http.sample.impl.DemoServiceImpl" />

    <dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:2181"/>

    <dubbo:protocol name="http" id="http" port="${servlet.port:8080}" server="${servlet.container:tomcat}"/>

    <dubbo:reference id="demoServiceImpl" interface="com.ipman.dubbo.http.sample.api.DemoService" protocol="http"/>

</beans>

```