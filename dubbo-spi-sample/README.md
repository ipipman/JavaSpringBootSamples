# 分布式服务框架之Dubbo（SPI机制）

### 什么是SPI机制？
##### Java SPI简介
在了解Dubbo的SPI机制之前，我们先了解下Java提供的SPI（service provider interface）机制，SPI是JDK内置的一种服务提供发现机制。目前市面上很多框架都用它来做服务的扩展发现。简单说它是一种动态替换发现的机制。

举个简单的例子，我们想在运行时动态添加实现，你只需要添加一个实现，然后把新的实现描述给JDK知道就行了。大家耳熟能详的如JDBC，日志框架都有用到。

##### 实现Java SPI需要遵循的标准
实现Java SPI需要满足如下标准：
1. 需要在classpath下创建一个目录，该目录名必须是：META-INF/service；
2. 在该目录下创建一个 properties 文件，该文件需要满足以下几个条件 ：
	2.1 文件名必须是扩展的接口的全路径名称；
	2.2 文件内部描述该扩展接口的所有实现类；
	2.3 文件的编码格式是UTF-8；
3. 通过 java.util.ServiceLoader 的加载机制来发现；

##### SPI的应用场景
- 这里我们以JDBC驱动为例：JDK本身提供了数据访问的api，在java.sql这个包里，我们在连接数据的时候会用到java.sql.Driver驱动接口。但是这个java.sql.Driver驱动接口在JDK本省并没有任何实现，而是提供了一套标准的api接口。

```java
package java.sql;
import java.util.logging.Logger;

public interface Driver {
    Connection connect(String url, java.util.Properties info)
        throws SQLException;
    boolean acceptsURL(String url) throws SQLException;
    DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info)
                         throws SQLException;
    int getMajorVersion();
    int getMinorVersion();
    boolean jdbcCompliant();
    public Logger getParentLogger() throws SQLFeatureNotSupportedException;
}
```

- 这里我们以mysql的JDBC驱动接口实现：mysql驱动也是通过SPI机制把java.sql.Driver和mysql驱动做了集成，这样就达到了各个数据库厂商自己去实现数据连接，JDK不关系你是怎么实现的。

<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/1041608699463_.pic.jpg" width = "740" height = "175" alt="图片名称" align=center />

##### SPI的缺点
- JDK标准的SPI会一次性加载实例化扩展点的所有实现。如果存在一些SPI的实现类并没有用到，那么初始化相对来说比较耗时；
- 如果扩展点加载失败，会导致调用方报错，而且这个错误很难定位；

### Dubbo中的SPI机制？
Dubbo SPI不是通过JDK的SPI机制实现的，而是自己实现了一套自己的SPI标准，Dubbo通过自身提供的@SPI注解声明扩展API，并提供默认的实现类。我们通过@Adaptive可以自动激活扩展点，替换Dubbo默认的实现类。

### 在Dubbo中有哪些功能能实现SPI扩展点？
<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/1001608375467_.pic.jpg" width = "700" height = "640" alt="图片名称" align=center />

Dubbo各层说明，除了Service和Config层为API，其它各层均为SPI
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

### Dubbo SPI实战（以Consumer端的Filter为例）
##### 1.实现Dubbo SPI需要遵循的标准
目录结构：
```java
src
 |-main
    |-java
        |-com
            |-xxx
                |-XxxFilter.java (实现Filter接口)
    |-resources
        |-META-INF
            |-dubbo
                |-org.apache.dubbo.rpc.Filter (纯文本文件，内容为：xxx=com.xxx.XxxFilter)
```


##### 2.实现Dubbo SPI扩展点Filter接口
需要注意的是Dubbo中的Filter分Consumer端和Provider端，可以通过@Activate配置过滤条件，比如gourp过滤和key过滤等；
```java
@Activate(group = CommonConstants.CONSUMER) //用于表示当前SPI扩展只在客户端被激活
public class CustomConsumerFilter implements Filter, Filter.Listener {

    //[ CustomConsumerFilter invoke ]
    // service=com.ipman.dubbo.spi.sample.api.DemoService，
    // method=sayHello，
    // args=["ipman"]
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        System.out.println("[ CustomConsumerFilter invoke ] service=" + invocation.getServiceName()
                + "，method=" + invocation.getMethodName() + "，args=" + JSON.toJSONString(invocation.getArguments()));
        if (("sayHello").equals(invocation.getMethodName())) {
            //throw new RpcException("You do not have access to this method");
        }
        return invoker.invoke(invocation);
    }

    //[ CustomConsumerFilter onResponse ]
    // service=com.ipman.dubbo.spi.sample.api.DemoService，method=sayHello，
    // args=["ipman"]，
    // response="Hello ipman, response from provider: 10.13.224.253:20880"
    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        System.out.println("[ CustomConsumerFilter onResponse ] service=" + invocation.getServiceName()
                + "，method=" + invocation.getMethodName() + "，args=" + JSON.toJSONString(invocation.getArguments())
                + "，response=" + JSON.toJSONString(appResponse.getValue()));
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
        System.out.println("[ CustomConsumerFilter onError ] service=" + invocation.getServiceName()
                + "，method=" + invocation.getMethodName() + "，args=" + JSON.toJSONString(invocation.getArguments())
                + "，error=" + t.getMessage());
    }
}
```
##### 3.配置META-INF.dubbo.org.apache.dubbo.rpc.Filter文件
```java
consumerFilter=com.ipman.dubbo.spi.sample.spi.CustomConsumerFilter
```


