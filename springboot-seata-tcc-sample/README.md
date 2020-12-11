# BASE柔性事务TCC模式之Seata分布式事务框架

### 一.什么是BASE柔性事务？

##### 如果将实现了ACID的事务要素的称为刚性事务的话（如数据库的分布式事务XA协议），那么基于BASE事务要素的事务则称为柔性事务。
> BASE是基于可用、柔性状态和最终一致性这三个要素
> - **基本可用**（Basically Available）保证分布式事务参与方不一定要同时在线；
> - **柔性状态**（Soft state）则允许系统状态更新有一定的延迟，这个延时对客户来说不一定能够察觉到；
> - **最终一致性**（Eventually consistent）通常是通过消息传递的方式保证系统的最终一致性；

------------

##### 对比本地事务 -> XA(2PC) -> BASE
 <img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/631607654308_.pic_hd.jpg" width = "600" height = "180" alt="图片名称" align=center />
 
 ------------

##### 分布式XA事务与分布式柔性事务的区别
> - **分布式XA事务，在ACID事务中对隔离性要求很高，在事务执行过程中，必须将所有资源锁定。**（因为XA事务和本地事务是互斥的）
> - **分布式柔性事务，理念则是通过业务逻辑层面将互斥锁操作从资源层面上移到业务层面。**（通过放宽强一致性要求，来换取系统吞吐量的提升）

------------

##### 柔性事务下事务特性，无法保证强一致性和隔离性，而是追求最终一致性
> - **原子性**（Atomicity）正常情况下保证
> - **一致性**（Consistency）在某个时间点，会出现A库和B库的数据违反一致性要求的情况，但是最终是一致性的
> - **隔离性**（Isolation）在某个时间点，A事务能够读到B事务部分提交的结果
> - **持久性**（Durability）和本地事务一样，只要commit则数据被持久

------------

### 二.柔性事务的TCC模式定义
##### 什么是TCC？
TCC不依赖RM对分布式事务的支持，而是通过对业务逻辑的分解来实现分布式事务，不同于AT的就是需要自行定义各个阶段的处理逻辑，对业务有浸入性
 <img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/701607671822_.pic_hd.jpg" width = "700" height = "400" alt="图片名称" align=center />

##### TCC模式将每个业务操作分为两个阶段
> - 第一个阶段检查并预留相关资源
> - 第二个阶段根据所有服务业务的Try状态来操作，如果成功，则进行Confirm操作，如果任意Try失败产生错误，则执行Cancel操作

##### TCC使用要求业务接口必须实现三段逻辑
> - 准备操作Try：完成所有业务检查，预留必须的业务资源
> - 确认操作Confirm：真正执行的业务逻辑，不做业务检查，只使用Try阶段预留的业务。因此只要Try成功，Confirm必须能成功。另外Confirm操作需满足幂等性，保证一笔分布式事务能且能执行一次。
> - 取消操作Cancel：释放Try阶段预留的业务资源。同样的，Cancel操作也需要满足幂等性

##### TCC使用时需要注意的几个问题
> - 允许空回滚：当Try未执行完成，会导致Cancel的空处理。解决方案，可以在Cancel中需要判断Try是否执行完成，如果未做完成，则不去执行
> - 防悬挂控制：因为网络抖动等原因，可以Cancel会在Try之前完成。解决方案，可以在执行Cancel时通过阻塞等待策略，直到Try执行完成后再执行
> - 幂等设计：因为网络抖动等原因，如数据库超时了，但是数据库却真的已执行了，此时再次重试会导致数据错误。解决方案，设计幂等机制，保证Try、Confirm、Cacnel操作只能执行一次

------------

### 三.Seata柔性事务框架
Seata是阿里开源的分布式框架，目前在分布式事务领域中应用最为广泛。
Seata 事务模型包含了：TM（事务管理器），RM（资源管理器）和TC（事务协调器）。
> - TC是一个独立部署的服务，TM和RM以jar包的方式同业务应用一同部署，它们同TC建立长连接，在整个事务生命周期内，保持远程通信。
> - TM是全局事务的发起放，负责全局事务的开启，提交和回滚。
> - RM是全局事务的参与者，负责分支事务的执行结果上报，并且通过TC协调器进行分支事务的提交和回滚。（注意TCC模式是不依赖RM的）

------------

##### Seata管理分布式事务的生命周期
 <img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/671607656675_.pic_hd.jpg" width = "600" height = "320" alt="图片名称" align=center />
 
1.  TM要求TC开始一个全局事务
2.  TC生成一个代表该全局事务的XID
3.  XID贯穿于微服务的整个调用链
4.  TM要求TC提交或回滚XID对应的全局事务
5.  TC驱动XID对应的全局事务下的所有分支事务完成提交和回滚

------------

##### Seata-TCC模式原理

###### 1.Seata-TCC的两阶段

 <img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/691607671761_.pic_hd.jpg" width = "700" height = "400" alt="图片名称" align=center />

全局事务是由若干分支事务组成的，分支事务要满足两阶段提交的模型要求，即需要每个分支事务都具备自己的:
> - 一阶段：自定义prepare行为
> - 二阶段：自定义commit 或 rollback行为

------------

### 四.Seata-TCC模式实战

###### 使用SpringBoot集成Duboo后注册两个服务，并且这两个服务都实现了Seata-TCC的两阶段性处理逻辑。（因为TCC模式并不依赖RM数据资源，所以这次测试并没有使用数据库）

 <img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/32361607671705_.pic_hd.jpg" width = "700" height = "400" alt="图片名称" align=center />

##### Seata-TCC模式实战：因篇幅有限，只对核心代码进行说明

###### 1.TCC注册分支事务：@TwoPhaseBusinessAction注解用于标注这是一个TCC接口，会初始化TccActionInterceptor拦截器，用于注册分支事务（Branch Transaction）
```java
    /**
     * 第一个阶段：准备阶段
     *
     * @param actionContext
     * @param a
     * @return
     */
    @TwoPhaseBusinessAction(name = "DubboTccActionOne", commitMethod = "commit", rollbackMethod = "rollback")
    public boolean prepare(BusinessActionContext actionContext,
                           @BusinessActionContextParameter(paramName = "a") int a);

    /**
     * 第二个阶段：提交操作
     *
     * @param actionContext
     * @return
     */
    public boolean commit(BusinessActionContext actionContext);

    /**
     * 第二个阶段：回滚阶段
     *
     * @param actionContext
     * @return
     */
    public boolean rollback(BusinessActionContext actionContext);
```

###### 2.开启TCC全局事务：在Seata中TCC模式和AT模式一样，业务方需要使用@GlobalTransactional注解来开启全局事务（Global Transaction），会初始化GlobalTransactionInterceptor拦截器，开启一个全局事务，获取全局的事务ID（即RootContext.getXID()）
```java
    /**
     * 分布式事务提交demo
     *
     * @return
     */
    @GlobalTransactional
    public String doTransactionCommit() {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");

        //第二个TCC 事务参与者
        result = tccActionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }

        //此时两个TCC参与者的prepare方法都执行成功了，TC就会触发这两个TCC参与者的commit方法进行提交
        return RootContext.getXID();
    }

    /**
     * 分布式事务回滚demo
     *
     * @param map
     * @return
     */
    @GlobalTransactional
    public String doTransactionRollback(Map<String, String> map) {
        //第一个TCC 事务参与者
        boolean result = tccActionOne.prepare(null, 1);
        if (!result) {
            throw new RuntimeException("TccActionOne failed.");
        }
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");

        //第二个TCC 事务参与者
        result = tccActionTwo.prepare(null, "two", list);
        if (!result) {
            throw new RuntimeException("TccActionTwo failed.");
        }
        map.put("xid", RootContext.getXID());

        //这里故意抛出异常，TC会触发这两个TCC参与者的rollback方法进行回滚
        throw new RuntimeException("transacton rollback");
    }

```

###### 3.测试流程:
> - 步骤一，运行SeataServerStarter（程序会在本地启动Seata Server服务）
> - 步骤二，运行SpringbootTccProviderApplication（程序会在本地使用Curator包自动启动Zookeeper服务，并启动Dubbo完成两个服务注册）
> - 步骤三，运行SpringbootTccTransactionApplication（程序会消费Dubbo的两个服务，运行Seata-TCC的分布式事务提交demo和分布式事务回滚demo）

------------

### 五.柔性事务中TCC、AT、SAGA模式总结

![https://raw.githubusercontent.com/ipipman/JavaKnowledge/master/XT/Base%E6%9F%94%E6%80%A7%E4%BA%8B%E5%8A%A1.png](https://raw.githubusercontent.com/ipipman/JavaKnowledge/master/XT/Base%E6%9F%94%E6%80%A7%E4%BA%8B%E5%8A%A1.png "https://raw.githubusercontent.com/ipipman/JavaKnowledge/master/XT/Base%E6%9F%94%E6%80%A7%E4%BA%8B%E5%8A%A1.png")