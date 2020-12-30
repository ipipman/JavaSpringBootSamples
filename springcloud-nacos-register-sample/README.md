# SpringCloud集成Nacos框架-实现注册中心

### 在讨论Nacos之前，我们先讨论一下CAP理论
CAP理论是分布式场景绕不开的重要理论
> - 一致性：所有节点在同一时间具有一样的数据；
> - 可用性：保证每个请求不管成功还是失败都有响应；
> - 分区容忍性：系统中任意信息的丢失和失败不会影响系统的继续运作；

<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/1081608882430_.pic_hd.jpg" width = "400" height = "280" alt="图片名称" align=center />

关于分区容忍性P的理解，大多数分布式系统都分布在多个子网络。每个子网络就叫做一个区（partition），分区容错的意思是，区间通信可能失败。比如，一台服务器放在中国，另一台服务器放在美国，这就是两个区，它们之间可能无法通信。

关于提高分区容忍性的办法，就是把同一份数据复制到多个节点上，分布到各个区里，容忍度就提高了。一般来说，分区容错无法避免，因此可以认为 CAP 的 P 总是成立。

剩下CAP的C和A无法同时做到，原因是
如果C是第一需求的话，那么会影响A的性能，因为要数据同步，不然请求结果就会有差异，但数据同步会消耗时间，期间可用性就会降低。

如果A是第一需求的话，那么只要有一个服务在，就能正常接受请求，但是对于返回结果变化不能保证一致性，原因是在分布式部署的时候，不能保障每个环境下处理速度。

### 主流注册中心或配置中心产品一致性对比
|   |  Nacos | Eureka  | Consul  | Zookeeper  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
|  CAP理论 | CP+AP  |  AP |  CP | CP  |
|   |   |   |   |   |   |

#### Apache Zookeeper -> CP
与Eureka有所不同，Apache Zookeeper在设计时就遵循CP原则，即任何时候对Zookeeper访问请求能得到一致的数据结果，同时系统对网络分区具备容错性，但是Zookeeper不能保证每次服务请求都是可用的。

从Zookeeper的实际应用情况来看，在使用Zookeeper获取服务列表时，如果此时Zookeeper集群中的Leader节点宕了，该集群要进行Leader的重新选举，又或者Zookeeper集群中半数节点不可用，都将无法处理请求，所以说Zookeeper不能保证服务可用性。

在大部分分布式环境中，尤其是设计数据存储的场景，数据一致性是首先要保证的，这也是Zookeeper设计CP原则的另一个原因。

但是对于服务发现来说，情况就不太一样了，针对同一个服务，即使注册中心的不同节点保存的服务提供者信息不同，也并不会造成灾难性后果。

因为对于服务消费者来说，能消费才是最重要的，消费者虽然拿到了可能不正确的服务提供者信息，也要胜过因无法获取实例而不去消费，导致系统异常要好。（消费者消费不正确的提供者信息可以进行补偿重试机制）

当master节点因为网络故障与其他节点失去联系时，剩余节点会重新进行leader选举，问题在于，选举Leader的时间太长，30～120s，而且选举期间整个zk集群是不可用的，这就导致整个选举期间注册服务瘫痪。

尤其在云部署环境下，因为网络问题使得ZK集群失去master节点是大概率事件，虽然服务能最终恢复，但是漫长的选举事件导致注册长期不可用是无法容忍的。

#### Spring Cloud Eureka -> AP

<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/1091608888792_.pic_hd.jpg" width = "630" height = "350" alt="图片名称" align=center />

Spring Cloud Netflix 在设计 Eureka的时候遵循的是AP
Eureka Server 也可以运行多个实例来构建集群，解决单点问题，但不同于Zookeeper选举leader的过程，Eureka Server采用的是Peer to Peer对等通信。这是一种去中性化的架构，无mater/salve之分，没一个Peer都是对等的。在这种架构风格中，节点通过彼此相互注册来提高可用性，每个节点需要添加一个或多个有效的serviceUrl指向其他节点。每个节点都可以视为其它节点的副本。

在集群环境中如果某台Eureka Server宕机，Eureka Client的请求会自动切换到新的Eureka Server节点上，当宕机的服务器重启恢复后，Eureka会再次将其纳入到服务器集群管理之中。当节点开始接受客户端请求时，所有的操作都会在集群中进行复制（replicate to peer）操作，将请求复制到该Eureka Server当前所知的所有节点上。

当一个新的Eureka Server节点启动后，会首先尝试从相邻节点获取所有注册列表信息，并完成初始化。Eureka Server通过getEurekaServiceUrls()方法获取所有的节点，并且会通过心跳契约的方式定时更新。

默认情况下，主要有一台Eureka还在，就能保证注册服务可用（保证可用性），只不过查到的信息可能不是最新的（不能保证强一致性）。除此之外，Eureka还有一种自我保护机制，如果在15分钟内超过85%的节点没有正常的心跳，那么Eureka就认为客户端与注册中心出现了网络故障，此时会出现以下几种情况：
> - Eureka不再从注册表中移除因为长时间没有收到心跳的服务；
> - Eureka仍然能够接受新服务注册和查询请求，但是不会被同步到其它节点上（既保证当前节点可用）；
> - 当网络稳定时，当前实例新注册的信息会被同步到其它节点上；

因此，Eureka可以很好的应对网络故障导致部分节点失去联系的情况，而不会像Zookeeper那样使得整个注册中心瘫痪。

#### Alibaba Nacos -> AP/CP

Nacos是阿里开源的一个产品，主要针对微服务架构中的服务发现、配置管理、服务治理的综合性解决方案；

Nacos支持两种方式的注册中心，持久化和非持久化存储服务信息。
> - 非持久化直接存储Nacos服务节点的内存中，并且服务节点采用去中心话的思想，服务节点采用Hash分片存储注册信息；
> - 持久化使用Raft协议选举Master节点，同样采用半同步机制将数据存储在Leader节点上；

Nacos同时支持持久化和非持久化存储，也就是支持CAP原则中的AP和CP特性，Nacos的CP支持持久化和ZK模式类似。Nacos默认采用AP非持久化，非持久化使用内存存储性能更快，而且Hash分片存储，不利点就是某个服务挂点，可能出现部分部分时间点用失败。因为服务调用本身就是实时的，持久化存储起来意义不大，而且及时变化更合适。

#### HashiCorp Consul -> CP

Consul是HashiCorp公司推出的开源工具，用于实现分布式系统的服务发现与配置。Consul使用Go编写，因此具有天然的移植性。

Consul内置了服务注册与发现框架、分布式一致性协议实现、健康检查、Key/Value存储、多数据中心方案。

Consul遵循CAP原理中的CP原则，保证了强一致性和分区容错性，且使用的是Raft算法，比Zookeeper使用的Paxos算法更加简单。虽然保证了强一致性，但是可用性就下降了，例如服务注册的时间会稍微长一些，因为Consul的Raft协议要求必须过半数的节点都写入成功才算成功，在Leader挂掉了之后，重新选出Leader之前会导致Consul不可用。

Consul Template
Consul 默认服务调用者需要依赖Consul SDK来发现服务，这就无法保证对应用的零入侵性。

通过Consul Template，可以定时从Consul集群获取最新的服务提供者列表并刷新Load Balance配置，这样对于服务调用者来说，只需要配置一个统一的服务调用地址即可。

Consul强一致性（C）带来的是：
> - 服务注册相比Eureka会稍慢一些，因为Consul的Raft协议要求必须半数节点都写入成功才算注册成功；
> - Leader挂掉时，重新选举期间整个Consul不可用，保证了强一致性但是牺牲了可用性；

Eureka保证高可用（A）和最终一致性：
> - 服务注册相对要快，因为不需要注册信息replicate到其他节点，也不保证注册信息是否replicate成功；
> - 当数据存在不一致时，虽然A，B上注册的信息不相同，但是每个Eureka节点依然能够正常的对外提供服务，这会出现查询服务信息时如果A查不到，但请求B就能查的到，保证了可用性但牺牲了一致性；

另一方面，Eureka就是个Servlet程序，跑到Servlet容器中。Consul则是go编写而成。

### 什么是Nacos？
Nacos是阿里开源的一个产品，主要针对微服务架构中的服务发现、配置管理、服务治理的综合性解决方案；
Nacos的四大功能：
> - 服务发现与服务健康检查;
> - 动态配置管理；
> - 动态DNS服务；
> - 服务和源数据管理；

### Zookeeper和Nacos的对比
Nacos在微服务场景中，主要用于配置中心和服务注册中心。这两块功能也是Zookeeper擅长的事情，以下我们分析下两者的不同处。

#### 1.Zookeeper
Zookeeper的功能主要是它的树型节点来实现的。当数据变化的时候或者节点过期的时候，会通过事件触发通知对应的客户端数据变化了，然后客户端再请求ZK获取最新的数据，采用push-pull来做数据更新；

其中ZK最重要的就是它的ZAB协议了（消息广播和消息恢复）
**消息广播**：集群中ZK在数据更新的时候，通过Leader节点将消息广播给其他Follower节点，采用简单的两阶段提交模式，先Request->ACK->Commit，当超过一半的Follower节点响应时，就可以提交更新了；

**奔溃恢复**：当Leader挂了，或者超过半数Follower投票得出Leader不可用，那么会重新选举，这段时间内ZK服务是不可用的。通过最新的XID来选举出新的Leader，选举出来的后将新的Leader中的数据更新给超过半数的Follower节点后，此时才能对外提供服务；

#### 2.Nacos
Nacos中配置中心和注册中心分别是两套实现，和ZK不同。

##### 2.1 对比配置中心
Nacos和Zookeeper都可以作为配置中心，做一些可以实时变化的配置数据存储，然后实时更新线上数据。

**Nacos**：依赖Mysql数据库做数据存储，当数据更新的时候，直接更新数据库的数据，然后将数据更新的信息异步广播给Nacos集群中所有服务节点数据变更，再由Nacos服务节点更新本地缓存，然后将通知客户端节点数据变化。

**Zookeeper**：利用ZK的树型结构做数据存储，当有数据更新的时候，使用过半机制保证各个节点的数据一致性，然后通过ZK的事件机制通知客户端。

这里的差异：
> - 服务器存储的位置不同，分别采用Mysql和ZK本身存储；
>
> - 消息发送，一个采用过半机制保证一致性，另一个异步广播，通过后台线程重试保证；

##### 2.2 对比注册中心

**Nacos**：支持两种方式的注册中心，持久化和非持久化存储服务信息。

> - **非持久化** 直接存储在Nacos服务节点的内存中，并且服务节点采用去中心话的思想，服务节点采用Hash分片存储注册信息；
>
> - **持久化** 使用Raft协议选举Master节点，同样采用过半机制将数据存储在Leader节点上；
>
**Zookeeper**：利用zk的树型结构做数据存储，服务注册和消费信息直接存储在zk树形节点上，集群下同样采用过半机制保证服务节点间一致性；

这里的差异：

> - Nacos同时支持 持久化和非持久化存储，也就是支持CAP原则中的AP和CP特性，Nacos的CP持久化和ZK模式类似。Nacos默认采用AP非持久化，非持久化使用内存存储速度更快，而且Hash分片存储，不利点就是某个服务挂掉，可能出现部分时间调用失败。因为服务调用本身就是实时的，持久化存储起来意义不大，反而及时变化更适合。


### Nacos的基础概念
<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/34201608729038_.pic_hd.jpg" width = "430" height = "300" alt="图片名称" align=center />

#### 命名空间
命名空间（NameSpace）用于不同环境（开发环境、测试环境和生产环境）的配置隔离。不同的命名空间下，可以存在相同名称的配置分组（Group）或配置集。

#### 配置分组
配置分组是对配置集进行分组，不同的配置分组下可以有相同的配置集（DateId）。默认的配置分组名称为 DEFAULT_GROUP。用于区分不同的项目或应用。

#### 配置集
在系统中，一个配置文件通常就是一个配置集，包含了系统各个方面的配置。


### Nacos-Server部署
#### 1.下载安装
```json
//下载编译后的最新zip包
https://github.com/alibaba/nacos/releases

//解压
unzip nacos-server-$version.zip
cd nacos/bin

//启动
sh startup.sh -m standalone
```

#### 2.测试注册中心和配置中心
```json
//服务注册
curl -X POST 'http://127.0.0.1:8848/nacos/v1/ns/instance?serviceName=nacos.naming.serviceName&ip=20.18.7.10&port=8080'

//服务发现
curl -X GET 'http://127.0.0.1:8848/nacos/v1/ns/instance/list?serviceName=nacos.naming.serviceName'

//发布配置
curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test&content=HelloWorld"

//获取配置
curl -X GET "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test"
```

#### 3.登录Nacos-Server控制台
<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/1651608729573_.pic.jpg" width = "620" height = "340" alt="图片名称" align=center />

```json
//访问
http://127.0.0.1:8848/nacos

//用户名&密码
nacos
nacos
```

### SpringCloud集成Nacos框架-实现注册中心
#### 1.添加Nacos依赖
```json
<dependency>
	<groupId>com.alibaba.cloud</groupId>
	<artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
	<version>2.1.3.RELEASE</version>
</dependency>
```

#### 2.添加Nacos注册中心配置（bootstrap.properties）
```java
#Nacos注册中心地址
spring.cloud.nacos.discovery.server-addr=10.211.55.6:8848
#命名当前服务名称
spring.cloud.nacos.discovery.service=${spring.application.name}
spring.cloud.nacos.discovery.enabled=true
```

#### 3.Spring入口类开启Ncaos注册
```java
@SpringBootApplication
@EnableDiscoveryClient //开启后向Nocas注册中心注册当前实例
public class SpringcloudNacosConfigSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudNacosConfigSampleApplication.class, args);
	}
}
```

#### 4.编写Provider端服务
```java
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    DemoService demoService;

    @GetMapping("hello")
    public Object echo(String name){
        return demoService.sayHello(name);
    }
}
```

#### 5.启动项目，查看Nacos控制台注册服务列表
<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/1191609313207_.pic_hd.jpg" width = "750" height = "310" alt="图片名称" align=center />
