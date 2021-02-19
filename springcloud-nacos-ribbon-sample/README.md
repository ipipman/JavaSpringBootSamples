# SpringCloud集成Nacos+Ribbon框架-实现注册中心和负载均衡

### Nacos注册中心部分请参考如下教程
- ##### [SpringCloud集成Nacos框架-实现注册中心;](https://github.com/ipipman/JavaSpringBootSamples/tree/master/springcloud-nacos-register-sample "SpringCloud集成Nacos框架-实现注册中心")

### 什么是Ribbon？
Ribbon是Netflix公司开源的基于客户端的负载均衡组件，可以简单理解成类似于Nginx的负载均衡模块的功能，它是Spring Cloud中非常重要的模块。在为分布式场景中，Ribbon与注册中心配合，为客户端调用远程服务提供了Load Balance负载均衡策略；

#### 主流的Load Balance方案分为两类
> - 一种是集中式Load Balance，即在服务的消费方和提供方直接使用独立的LB设施（如ngixn、h5），由该设施方负责把访问请求通过某种策略转发至服务端。
> - 另一种是进程内的Load Balance，将LB逻辑集成到消费方，消费方从服务注册中心获取有哪些服务地址可用，然后自己再从这些地址中选择出一个合适的服务器地址。Ribbon和Dubbo LoadBalance都属于这种方式，它只是一个类库，集成于消费方进程中，消费方通过它来获取到服务提供方地址。

#### 使用负载均衡的好处
> - 当集群里的某台服务器宕机的时候，剩余的没有宕机服务可以保证服务继续使用。
> - 更加方便合理使用了更多的机器保证了良性使用，不会由于某一高峰时刻导致系统崩溃。

**负载均衡具有的负载算法如下**
> - 随机的（Random）
> - 轮询（RoundRobin）
> - 一致性哈希（ConsistentHash）
> - 哈希（Hash）
> - 加权（Weighted）

#### Ribbon的核心接口组成部分
| 接口  | 作用  | 默认值  |
| ------------ | ------------ | ------------ |
| IClientConfig  | 读取配置  | DefultClientConfigImpl  |
| IRule  | 负载均衡规则，选择实例  | ZoneAvoidanceRule  |
| IPing  | 筛选掉Ping不通的实例  | DummyPing  |
| ServerList<Server>  | 交给Ribbon的实例列表  | 来源Eureka、Nacos、Consul等注册中心  |
| ServerFiter<Server>  | 过滤不符合条件的实例  | ZonePreferenceServerListFilter  |
| ILoadBalance  | Ribbon的入口  | ZoneAwareLoadBalance  |
| ServerListUpdater  | 更新交给Ribbon的List策略  | PollingServerListUpdater  |

Ribbon是比较灵活的，它对所有的组件都定义成了接口，如果对默认值不满意，可以实现这些接口配置一下，就可以将默认实现替换掉

####  ILoadBalance 负载均衡器
Ribbon是一个为客户端提供负载均衡功能的服务，他内部提供了一个叫ILoadBalance的接口代表负载均衡器的操作，比如添加服务操作、选择服务器操作、获取所有的服务器列表、获取可用的服务器列表等等。

####  ILoadBalance 的实现类源码分析
<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/1161609242294_.pic.jpg" width = "440" height = "400" alt="图片名称" align=center />

负载均衡器可以从服务发现组建（NacosDiscoveryClient、ConsulDisconveryClient、EurekaClient）获取服务信息，根据IRule去路由，并且根据IPing判断服务的可用性。

在BaseLoadBalance类构造函数中，开启了一个PingTask任务setupPingTask();
```java
public BaseLoadBalancer(String name, IRule rule, LoadBalancerStats stats, IPing ping, IPingStrategy pingStrategy) {
        this.rule = DEFAULT_RULE;
        this.pingStrategy = DEFAULT_PING_STRATEGY;
        this.ping = null;
        this.allServerList = Collections.synchronizedList(new ArrayList());
        this.upServerList = Collections.synchronizedList(new ArrayList());
        this.allServerLock = new ReentrantReadWriteLock();
        this.upServerLock = new ReentrantReadWriteLock();
        this.name = "default";
        this.lbTimer = null;
        this.pingIntervalSeconds = 10;
        this.maxTotalPingTimeSeconds = 5;
        this.serverComparator = new ServerComparator();
        this.pingInProgress = new AtomicBoolean(false);
        this.counter = Monitors.newCounter("LoadBalancer_ChooseServer");
        this.enablePrimingConnections = false;
        this.changeListeners = new CopyOnWriteArrayList();
        this.serverStatusListeners = new CopyOnWriteArrayList();
        logger.debug("LoadBalancer [{}]:  initialized", name);
        this.name = name;
        this.ping = ping;
        this.pingStrategy = pingStrategy;
        this.setRule(rule);
        this.setupPingTask();
        this.lbStats = stats;
        this.init();
    }
```

setupPingTask()开启了ShutdownEnabledTimer执行PingTask任务，在默认情况下pingIntervalSeconds为10，即每10秒，向EurekaClient、NacosClient、ConsulClient等发送一次“ping”任务。
```java
void setupPingTask() {
        if (!this.canSkipPing()) {
            if (this.lbTimer != null) {
                this.lbTimer.cancel();
            }

            this.lbTimer = new ShutdownEnabledTimer("NFLoadBalancer-PingTimer-" + this.name, true);
            this.lbTimer.schedule(new BaseLoadBalancer.PingTask(), 0L, (long)(this.pingIntervalSeconds * 1000));
            this.forceQuickPing();
        }
    }
```

PingTask源码，即new一个Pinger对象，并执行runPinger()方法。

查看Pinger的runPinger()方法，最终根据pingerStrategy.pingServers(ping, allServers)来获取服务的可用性，如果该返回结果相同，则不去向EurekaClient、NacosClient、ConsulClient获取注册列表，如果不同则通知ServerStatusChangeListener或者changeListeners发生了改变，进行更新或者重新拉取。

完整过程是：
LoadBalancerClient（RibbonLoadBalancerClient是实现类）在初始化的时候（execute方法），会通过ILoadBalance（BaseLoadBalancer是实现类）向Eureka注册中心获取服务注册列表，并且每10s一次向EurekaClient、NacosClient、ConsulClient等发送“ping”，来判断服务的可用性，如果服务的可用性发生了改变或者服务数量和之前的不一致，则从注册中心更新或者重新拉取。LoadBalancerClient有了这些服务注册列表，就可以根据具体的IRule来进行负载均衡。

#### IRule 路由
IRule接口代表负载均衡策略：
```java
public interface IRule {
    Server choose(Object var1);
    void setLoadBalancer(ILoadBalancer var1);
    ILoadBalancer getLoadBalancer();
}
```
#### Ribbon内置的负载均衡规则：
| 规则名称  | 特点  |
| ------------ | ------------ |
| RandomRule   | 随机选择一个Server  |
| RoundRobinRule   | 轮询选择，轮询index，选择index对应位置的Server  |
| WeightedResponseTimeRule   | 根据响应时间加权，响应时间越长，权重越小，被选中的可能性越低  |
| BestAvailableRule   | 选择一个最小的并发请求的server，逐个考察server，如果Server被tripped了，则跳过  |
| ...  |   |   |


- 随机策略很简单，就是从服务器中随机选择一个服务器，RandomRule的实现代码如下
- RoundRobinRule轮询策略表示每次都取下一个服务器，比如一共有5台服务器，第1次取第1台，第2次取第2台，第3次取第3台，以此类推;
- WeightedResponseTimeRule继承了RoundRobinRule，开始的时候还没有权重列表，采用父类的轮询方式，有一个默认每30秒更新一次权重列表的定时任务，该定时任务会根据实例的响应时间来更新权重列表，choose方法做的事情就是，用一个(0,1)的随机double数乘以最大的权重得到randomWeight，然后遍历权重列表，找出第一个比randomWeight大的实例下标，然后返回该实例.
- BestAvailableRule策略用来选取最少并发量请求的服务器


###  SpringCloud集成Nacos+Ribbon框架-实现注册中心和负载均衡实战
####  1.参考Nacos注册中心部分请参考如下教程，启动Nacos集群与服务提供者
- ##### [SpringCloud集成Nacos框架-实现注册中心;](https://github.com/ipipman/JavaSpringBootSamples/tree/master/springcloud-nacos-register-sample "SpringCloud集成Nacos框架-实现注册中心")

####  2.添加Ribbon依赖
```java
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
	<version>2.1.4.RELEASE</version>
</dependency>
```

####  3.添加Ribbon和Nacos相关配置
```java
server.port=8308
spring.application.name=nacos-ribbon-service

#Nacos注册中心地址
spring.cloud.nacos.discovery.server-addr=10.211.55.6:8848
#命名当前服务名称
spring.cloud.nacos.discovery.service=${spring.application.name}
spring.cloud.nacos.discovery.enabled=true

#需要启动provider服务：https://github.com/ipipman/JavaSpringBootSamples/tree/master/springcloud-nacos-register-sample
service.url.nacos.provider.service=http://nacos-provider-service

#开启Ribbon的饥饿加载模式
ribbon.eager-load.enabled=true
#指定需要饥饿加载的服务名
ribbon.eager-load.clients=consul-provider-service
#Ribbon的超时
ribbon.ConnectTimeout=3000
ribbon.ReadTimeout=60000
#对第一次请求的服务的重试次数
ribbon.MaxAutoRetries=1
#要重试的下一个服务的最大数量（不包括第一个服务）
ribbon.MaxAutoRetriesNextServer=1
#无论是请求超时或者socket read timeout都进行重试
ribbon.OkToRetryOnAllOperations=true
#默认随机策略
ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule
```

####  4.自定义Load Balance策略
```java
//Ribbon更细力度的配置，可以针对不同服务设置Load Balance策略
@Configuration
public class XXProviderRibbonConfig {

    //BestAvailableRule策略用来选取最少并发量请求的服务器
    @Bean
    public IRule ribbonRule(){
        return new BestAvailableRule();
    }
}
```

```java
@Configuration
//RibbonClients可以配置多个服务策略
@RibbonClients({
        //Ribbon更细力度的配置，可以针对不同服务设置Load Balance策略
        @RibbonClient(name = "consul-provider-service", configuration = XXProviderRibbonConfig.class)
})
public class RibbonConfig {

    //使用RestTemplate进行rest操作的时候，会自动使用负载均衡策略，它内部会在RestTemplate中加入LoadBalancerInterceptor这个拦截器，这个拦截器的作用就是使用负载均衡。
    //默认情况下会采用轮询策
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

####  5.在接口中使用Ribbon调用远程服务
```java
@RestController
@RequestMapping("/nacos")
public class DemoController {

    @Value("${service.url.nacos.provider.service}")
    private String serviceUrl;

    @Autowired
    private RestTemplate restTemplate;

    //使用Ribbon全局维度的配置
    @GetMapping("/ribbon")
    public Object goService(String name) {
        return restTemplate.getForObject(serviceUrl + "/demo/hello?name={1}", String.class, name);
    }
}
```

####  6.启动项目，进行测试
```java
% curl -l 'http://127.0.0.1:8308/nacos/ribbon?name=ipman'     
Hello ipman, response from provider: http://10.13.224.186:8081
```

####  7.在Nacos控制台查看Ribbon服务状态
<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/1201609324365_.pic_hd.jpg" width = "740" height = "340" alt="图片名称" align=center />

