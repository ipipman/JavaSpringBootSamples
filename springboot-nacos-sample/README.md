# SpringBoot集成Nacos框架-实现配置中心

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

** Zookeeper**：利用ZK的树型结构做数据存储，当有数据更新的时候，使用过半机制保证各个节点的数据一致性，然后通过ZK的事件机制通知客户端。

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

### Springboot集成Nacos为配置中心
#### 1.添加Nacos依赖
```json
		<dependency>
			<groupId>com.alibaba.boot</groupId>
			<artifactId>nacos-config-spring-boot-starter</artifactId>
			<version>0.2.1</version>
		</dependency>
```

#### 2.配置application.properties
```json
server.port=8080
nacos.config.server-addr=10.211.55.6:8848
```

#### 3.通过@NacosPropertySource 加载 dataId 并设置注册中心，通过 Nacos 的 @NacosValue 注解设置属性值
```json
@Controller
@RequestMapping("config")
//使用 @NacosPropertySource 加载 dataId 为 example 的配置源，并开启自动更新
@NacosPropertySource(dataId = "nacos.cfg.test", autoRefreshed = true)
public class NacosController {

    //通过 Nacos 的 @NacosValue 注解设置属性值
    @NacosValue(value = "${nacos.test.propertie:null}", autoRefreshed = true)
    private String testProperties;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public String get() {
        return testProperties;
    }
}
```

#### 4.在Nacos控制台设置dataId，并发布配置文件
<img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/dubbo/1071608735439_.pic_hd.jpg" width = "720" height = "340" alt="图片名称" align=center />

#### 5.启动SpringBoot项目，通过接口进行测试
```json
curl -i -l http://127.0.0.1:8080/config/get
```
#### 6.在Nacos控制台修改配置，并完成配置推送，再次执行步骤5查看更改后效果

