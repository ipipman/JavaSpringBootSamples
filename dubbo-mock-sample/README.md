# 分布式服务框架之Dubbo（Mock功能）

### Mock功能
在开发自测，联调过程中，经常碰到一些下游服务调用不成功的场景，这个时候我们可以通过Mock功能跳过下游服务依赖，独立完成自测；
Dubbo自身是支持mock服务的，在Reference标签里，有一个mock参数：
> - flase，不调用mock服务；
> - true，当服务调用失败时，使用mock服务；
> - default，当服务调用失败时，使用mock服务；
> - force，强制使用mock服务，不管服务是否调用成功；

### Mock实战
#### 1.Mock的使用规则，需要在服务接口目录下，创建一个与接口同名的Mock实现类
```java
//服务接口
public interface MockService {
    String sayHello(String name);
}
```
```java
//定义Mock的实现类（注意类的名称=接口名称+Mock关键字）
public class MockServiceMock implements MockService {
    @Override
    public String sayHello(String name) {
        return "Response from Mock sayHello，" + name;
    }
}
```

#### 2.实现业务，模拟服务响应超时异常
```java
public class MockServiceImpl implements MockService {
    @Override
    public String sayHello(String name) {
        try {
            // 休眠5秒会导致客户端的TimeoutException，并且会调用mock impl
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello " + name;
    }
}
```
#### 3.配置Consumer，在Reference中设置mock=true，也就是说当服务调用失败时，使用mock服务
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd">

    <dubbo:application name="mock-consumer"/>

    <dubbo:registry address="zookeeper://127.0.0.1:2181"/>

    <dubbo:reference id="mockService" check="false" interface="com.ipman.dubbo.mock.sample.api.MockService"
                     mock="true"/>
</beans>
```