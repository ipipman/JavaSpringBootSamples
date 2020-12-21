# 分布式服务框架之Dubbo（参数回调）

### Dubbo实现的几种调用方式
Dubbo协议采用单一长连接，底层实现是Netty的NIO异步通讯机制；基于这种机制，Dubbo实现了以下几种调用方式：
> - 同步调用；
> - 异步调用；
> - 参数回调；
> - 事件通知；

### 同步调用
同步调用是一种阻塞的调用方式，即Consumer端代码一直阻塞等待，直到Provider端返回为止；
通常，一个典型的同步调用过程如下：
1. Consumer业务线程调用远程接口，向Provider发送请求，同时当前线程处于阻塞状态；
2. Provider接到Consumer的请求后，开始处理请求，将结果返回给Consumer；
3. Consumer收到结果后，当前线程继续往后执行；

这里有2个问题：
1. Consumer业务线程是怎么进入阻塞状态的？
2. Consumer收到结果后，如何唤醒业务线程往后执行的？

其实，Dubbo的底层IO操作都是异步的。Consumer端发起调用后，得到一个Future对象。对于同步调用，业务线程通过Future.get(timeout)，阻塞等待Provider端将结果返回。timeout则是Consumer定义的超时时间。当结果返回后，会设置到此Future，并且唤醒阻塞的业务线程，当超时时间到结果还未返回时，业务线将会异步返回。

### 异步调用
给予Dubbo底层的异步NIO实现异步调用，对于Provider响应时间较长的场景是必须的，它能有效的利用Consumer的资源，相对于Consumer端使用多线程来说开销较小；

### 事件通知
事件通知允许Consumer端在调用之前、之后或出现异常时，触发oninvoke、onreturn、onthrow三个事件
自定义Notify接口中三个方法的参数规则如下：
1. oninvoke 方法参数与调用方法的参数相同；
2. onreturn 方法第一个参数为调用方法的返回值，其余为调用方法的参数；
3. onthrow 方法第一个参数为调用异常，其余为调用方法的参数；

### 参数回调
参数回调有点类似本地Callback机制，但Callback并不是Dubbo内部的类或接口，而是由Provider端自定义的。Dubbo将基于长连接生成反向代理，从而实现从Provider端调用Consumer端逻辑。

### Dubbo参数回调实战
#### 1、Provider配置
```java
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:property-placeholder/>

    <dubbo:application name="callback-provider"/>

    <dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:2181"/>

    <dubbo:protocol name="dubbo" port="20880"/>

    <bean id="callbackService" class="com.ipman.dubbo.callback.sample.impl.CallbackServiceImpl"/>

    <dubbo:service interface="com.ipman.dubbo.callback.sample.api.CallbackService" ref="callbackService"
                   connections="1" callbacks="1000">
        <!--    监听者方法    -->
        <dubbo:method name="addListener">
            <dubbo:argument index="1" callback="true"/>
        </dubbo:method>
    </dubbo:service>

</beans>
```
#### 2、Provider端Service实现
```java
public class CallbackProvider {

    private static TestingServer zkServer;

    public static void main(String[] args) {
        //启动ZK
        mockZookeeper();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/callback-provider.xml");
        context.start();
    }

    //启动ZK
    @SneakyThrows
    public static void mockZookeeper(){
        zkServer = new TestingServer(2181, true);
        zkServer.start();
    }
}

```

#### 3、Consumer配置
```java
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:property-placeholder/>

    <dubbo:application name="callback-consumer"/>

    <dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:2181"/>

    <dubbo:reference id="callbackService" interface="com.ipman.dubbo.callback.sample.api.CallbackService"/>
</beans>
```