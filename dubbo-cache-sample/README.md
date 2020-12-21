# 分布式服务框架之Dubbo（缓存策略）

### Dubbo缓存介绍
针对结果缓存，用于加速热点数据访问速度，Dubbo提供声明式缓存，以减少用户加缓存工作量；

### 缓存类型
####  ThreadLocal
该缓存应用场景为:
> - 比如一个请求需要多次访问服务，可以通过线程缓存，可以减少多余访问；
> - 场景描述的核心内容是当前请求的上下文；

配置示例：
```xml
<dubbo:reference interface="com.demo.xx" cache="threadlocal" />
```

####  LRU缓存淘汰算法（Dubbo默认的缓存策略）
该缓存应用场景为:
> - LRU基于最近最少使用的原则删除多余缓存，保持最热的数据被缓存；
> - 该类型的缓存是跨线程的；

##### LRU原理
LRU（Least recently used，最近最少使用）算法根据数据历史访问记录进行淘汰数据，其核心思想就是“如果数据最近被访问过，那么将来访问的几率也更高”；

最常见的实现是使用一个链表保存缓存数据：
1. 新数据插入链表头部；
2. 每当缓存命中（即缓存数据被访问），则将数据移到链表头部；
3. 当链表满的时候，将链表尾部数据丢弃；

配置示例：
```xml
<dubbo:reference interface="com.demo.xx" cache="true" />
```

####  JCache
Jcache与JSR107集成，可以桥接各种缓存实现；

### Dubbo缓存实战
#### 1.配置Provider端
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:property-placeholder/>

    <dubbo:application name="cache-provider"/>

    <dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:2181"/>

    <dubbo:protocol name="dubbo" port="20880"/>

    <bean id="cacheService" class="com.ipman.dubbo.cache.sample.service.impl.CacheServiceImpl"/>

    <dubbo:service interface="com.ipman.dubbo.cache.sample.service.CacheService" ref="cacheService"/>
</beans>
```

#### 2.配置Consumer端
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:property-placeholder/>

    <dubbo:application name="cache-consumer"/>

    <dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:2181"/>

    <dubbo:reference id="cacheService" interface="com.ipman.dubbo.cache.sample.service.CacheService" cache="true"/>
</beans>
```

#### 3.测试LRU淘汰算法
```java
public class CacheConsumer {

    @SneakyThrows
    public static void main(String[] args) {
        //启动消费者
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/cache-consumer.xml");
        CacheService cacheService = (CacheService) context.getBean("cacheService");

        //对比结果：True，命中缓存
        String value = cacheService.findCache(0);
        System.out.println(value.equals(cacheService.findCache(0)));

        //默认使用LRU淘汰算法，默认缓存Size是1000，如果调用1001次缓存则失效
        for (int i = 1; i <= 1001; i++) {
            cacheService.findCache(i);
        }

        //对比结果：Flase，缓存失效
        System.out.println(value.equals(cacheService.findCache(0)));
    }
}
```
