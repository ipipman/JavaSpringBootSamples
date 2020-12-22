# 分布式服务框架之Dubbo（本地调用）

### 本地调用
在Dubbo中进行本地调用，本地调用使用了injvm协议，是一个伪协议，它不开启端口，不发起远程调用，只是在JVM内直接关联，但执行Dubbo的Filter链。

### Dubbo本地调用实战
#### 1.配置服务，定义Provider协议为injvm
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:property-placeholder/>

    <dubbo:application name="demo-provider"/>

    <dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:2181"/>

    <dubbo:protocol name="dubbo" port="20890"/>

    <bean id="target" class="com.ipman.dubbo.local.sample.impl.DemoServiceImpl"/>

    <dubbo:service interface="com.ipman.dubbo.local.sample.api.DemoService" ref="target" protocol="injvm" />

    <dubbo:reference id="demoService" interface="com.ipman.dubbo.local.sample.api.DemoService"/>
</beans>
```
#### 2.打印生产者和消费者端口，查看测试结果，可以看的出来，在调用定义了injvm协议的服务时，它不开启端口，不发起远程调用
```java
public class LocalDubbo {

    private static TestingServer zkServer;

    public static void main(String[] args) {
        //启动ZK
        mockZookeeper();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/local-dubbo.xml");
        context.start();

        DemoService demoService = context.getBean("demoService", DemoService.class);

        //Hello ipman, response from provider: 127.0.0.1:0
        System.out.println(demoService.sayHello("ipman"));
    }

    //启动ZK
    @SneakyThrows
    public static void mockZookeeper() {
        zkServer = new TestingServer(2181, true);
        zkServer.start();
    }
}
```