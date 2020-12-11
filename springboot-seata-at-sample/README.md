# BASE柔性事务AT模式之Seata分布式事务框架

### 1.什么是BASE柔性事务？

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

### 2.柔性事务的AT模式定义
##### AT模式就是两阶段提交，自动生成反向SQL
 <img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/641607656083_.pic_hd.jpg" width = "600" height = "230" alt="图片名称" align=center />

------------

### 3.Seata柔性事务框架
Seata是阿里开源的分布式框架，目前在分布式事务领域中应用最为广泛。
Seata AT 事务模型包含了：TM（事务管理器），RM（资源管理器）和TC（事务协调器）。
> - TC是一个独立部署的服务，TM和RM以jar包的方式同业务应用一同部署，它们同TC建立长连接，在整个事务生命周期内，保持远程通信。
> - TM是全局事务的发起放，负责全局事务的开启，提交和回滚。
> - RM是全局事务的参与者，负责分支事务的执行结果上报，并且通过TC协调器进行分支事务的提交和回滚。

------------

##### Seata管理分布式事务的生命周期
 <img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/671607656675_.pic_hd.jpg" width = "600" height = "320" alt="图片名称" align=center />
1. TM要求TC开始一个全局事务
2. TC生成一个代表该全局事务的XID
3. XID贯穿于微服务的整个调用链
4. TM要求TC提交或回滚XID对应的全局事务
5. TC驱动XID对应的全局事务下的所有分支事务完成提交和回滚

------------

##### Seata-AT原理

###### 1.Seata-AT的两阶段

 <img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/651607656115_.pic_hd.jpg" width = "700" height = "400" alt="图片名称" align=center />

Seata-AT两阶段提交协议的演变:
> - 一阶段：业务数据和回滚日志记录在同一个本地事务中提交，释放本地锁和连接资源
> - 二阶段：提交异步化，非常快速地完成。回滚通过一阶段日志进行方向SQL补偿。

------------

###### 2.Seata-AT如何实现多个事务的读写隔离

 <img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/661607656128_.pic_hd.jpg" width = "700" height = "400" alt="图片名称" align=center />

Seata-AT通过全局锁的方式，实现读写隔离
> - 本地锁控制本地操作
> - 全局锁控制全局提交

------------

### Seata-AT实战
##### 1.实现一个交易业务（Bussiness），需要库存服务（Stroage）、订单服务（Order）和用户服务（Account）
 <img src="https://raw.githubusercontent.com/ipipman/JavaSpringBootSamples/master/ReadmeMaterial/681607659883_.pic.jpg" width = "600" height = "320" alt="图片名称" align=center />
###### 在SprintBoot下运行，本文并没有使用RPC的方式，而是在HTTP请求头中透传XID的方式实现的，代码如下：
> - seata-at-account-service
> - seata-at-bussiness-service
> - seata-at-stroage-service
> - seata-at-order-service


------------

##### 2.SQL准备工作
```sql
# db_seata
DROP SCHEMA IF EXISTS db_seata;
CREATE SCHEMA db_seata;
USE db_seata;

# Account
CREATE TABLE `account_tbl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `money` INT(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

INSERT INTO account_tbl (id, user_id, money)
VALUES (1, '1001', 10000);
INSERT INTO account_tbl (id, user_id, money)
VALUES (2, '1002', 10000);

# Order
CREATE TABLE `order_tbl`
(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `commodity_code` VARCHAR(255) DEFAULT NULL,
  `count` INT(11) DEFAULT '0',
  `money` INT(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

# Storage
CREATE TABLE `storage_tbl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `commodity_code` VARCHAR(255) DEFAULT NULL,
  `count` INT(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `commodity_code` (`commodity_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;


INSERT INTO storage_tbl (id, commodity_code, count)
VALUES (1, '2001', 1000);

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

------------


##### 3.安装Seata
- 下载Seata
> wget https://github.com/seata/seata/releases/download/v1.0.0/seata-server-1.0.0.zip

-  解压Seata
> yum install unzip -y
> unzip -o seata-server-1.0.0.zip
> cd  seata-server-1.0.0

-  修改配置
> vi ./conf/file.conf
> 使用file模式
```shell
service {
  default.grouplist = "10.211.55.6:8091"
  disableGlobalTransaction = false
}
store {
  mode = "file"
  file {
    dir = "sessionStore"
  }
  db {
    datasource = "dbcp"
    db-type = "mysql"
    driver-class-name = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://127.0.0.1:3306/seata"
    user = "root"
    password = "root"
  }
}
```
-  启动Seata
> sh ./bin/seata-server.sh


##### 4.启动四个SpringBoot项目，注意数据库和Seata服务的IP与端口配置，进行测试
> 测试成功场景：curl -X POST http://127.0.0.1:8084/api/business/purchase/commit
> 测试事务回滚场景：http://127.0.0.1:8084/api/business/purchase/rollback

