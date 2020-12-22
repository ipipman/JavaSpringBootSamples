# XA事务之Atomikos分布式事务框架

### 一.什么是XA事务？

##### 1.XA协议定义

> - 基于一个强一致的思路，就有了基于数据库本身支持的协议，XA分布式事务。
>
> - XA整体设计思路可以概括为，如何在现有事务模型上微调扩展，实现分布式事务。
 
##### 2.XA协议成员
  <img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/711607681004_.pic_hd.jpg" width = "600" height = "240" alt="图片名称" align=center />

> - 应用程序(Application Program ，简称 AP):用于定义事务边界(即定义事务的开始和 结束)，并且在事务边界内对资源进行操作。
>
> - 资源管理器(Resource Manager，简称 RM):如数据库、文件系统等，并提供访问资源 的方式
>
> - 事务管理器(Transaction Manager ，简称 TM):负责分配事务唯一标识，监控事务的执行 进度，并负责事务的提交、回滚等。

##### 3.XA协议接口
  <img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/721607681013_.pic_hd.jpg" width = "400" height = "450" alt="图片名称" align=center />

###### 3.1XA也是2PC（两阶段）的，第一阶段（xa_start、xa_end），第二阶段（xa_prepare、xa_commit、xa_rollback）

> - xa_start :负责开启或者恢复一个事务分支
>
> - xa_end: 负责取消当前线程与事务分支的关联
>
> - xa_prepare:询问 RM 是否准备好提交事务分支
>
> - xa_commit:通知 RM 提交事务分支
>
> - xa_rollback: 通知 RM 回滚事务分支
>
> - xa_recover : 需要恢复的 XA 事务

##### 4.Mysql中对XA协议的支持
###### 4.1 MySQL 从5.0.3开始支持InnoDB引擎的 XA 分布式事务，MySQL Connector/J 从5.0.0版本开始支持XA。（Mysql5.7以下版本XA是不安全的，如Mysql5.6断开SQL连接后，XA会自动回滚，导致无法重连补偿）
  <img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/731607681029_.pic_hd.jpg" width = "800" height = "220" alt="图片名称" align=center />
   
###### 4.2 在 DTP 模型中，MySQL 属于资源管理器(RM)。分布式事务中存在多个 RM，由事务管理器 TM 来统一进行协调。
  <img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/741607681043_.pic_hd.jpg" width = "800" height = "180" alt="图片名称" align=center />

##### 5 MySQL XA事务状态
  <img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/751607681057_.pic_hd.jpg" width = "800" height = "500" alt="图片名称" align=center />

> - SQL执行完成后进入XA_END状态
>
> - XA_END状态可以进入XA_PREPARE状态
>
> - XA_PREPARE状态可以进入XA_COMMIT和XA_ROLLBACK状态
>
> - XA事务和本地事务的开启是互斥的，不能同时进行

##### 6 XA事务失败了怎么办？
> - **业务SQL执行过程中，某个RM崩溃怎么处理？**
>
>     如果在start和end阶段报错，还未到prepare阶段，TM会回滚全部XA事务.
>
> - **全部prepare后，某个RM崩溃怎么处理？**
>
>     TM等待RM恢复后，重新连接，再次提交.
>
> - **commit时，某个RM崩溃时如何处理？**
>
>     没有好办法，通过Recover重试或人工.

##### 7 XA协议可能会存在的问题
> - **同步阻塞问题**
>
>     多台Mysql中Commit的速度是不一致的，在多个XA事务并发的场景下可能会存在脏数据
>
>     如果一定要保证全局事务一致性，需要调整隔离级别为串行话（Serializable）
>
> - **数据不一致**
>
>     极端情况下，一定有事务失败问题，需要监控和人工处理

------------

### 二.XA框架
##### 目前XA应用较为广泛的是Atomikos框架
  <img src="https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/761607681074_.pic_hd.jpg" width = "800" height = "320" alt="图片名称" align=center />
   
### 三.Atomikos-XA框架实战
##### 1.SQL准备工作，创建两个数据库和相应的表结构
```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo
-- ----------------------------
CREATE database test;
use test;
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;


CREATE database test1;
use test1;
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;

```

##### 2.配置AtomikosDataSourceBean数据源和JtaTransactionManager事务管理器
```java
@Configuration
public class AtomikosDataSourceConfig {

    @Value("${db1.datasource.url}")
    private String db1Url;

    @Value("${db2.datasource.url}")
    private String db2Url;

    @Value("${db1.datasource.username}")
    private String db1UserName;

    @Value("${db2.datasource.username}")
    private String db2UserName;

    private final static String XA_DS_CLASS_NAME = "com.mysql.cj.jdbc.MysqlXADataSource";

    /**
     * 第一个数据库
     *
     * @return
     */
    @Bean(name = "db1DataSource")
    @Primary
    @Qualifier("db1DataSource")
    public AtomikosDataSourceBean db1DataSourceBean() {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("db1DataSource");
        atomikosDataSourceBean.setXaDataSourceClassName(XA_DS_CLASS_NAME);
        Properties properties = new Properties();
        properties.put("URL", db1Url);
        properties.put("user", db1UserName);
        properties.put("password", "");
        atomikosDataSourceBean.setXaProperties(properties);
        return atomikosDataSourceBean;
    }

    /**
     * 第二个数据库
     *
     * @return
     */
    @Bean(name = "db2DataSource")
    @Qualifier("db2DataSource")
    public AtomikosDataSourceBean db2DataSourceBean() {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("db2DataSource");
        atomikosDataSourceBean.setXaDataSourceClassName(XA_DS_CLASS_NAME);
        Properties properties = new Properties();
        properties.put("URL", db2Url);
        properties.put("user", db2UserName);
        properties.put("password", "");
        atomikosDataSourceBean.setXaProperties(properties);
        return atomikosDataSourceBean;
    }

    @Bean(destroyMethod = "close", initMethod = "init")
    public UserTransactionManager userTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    /**
     * 定义XA事务管理器（TM）
     *
     * @return
     */
    @Bean(name = "XATransactionManager")
    public JtaTransactionManager jtaTransactionManager() {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        return jtaTransactionManager;
    }
}
```

##### 3.获取Atomikos数据库连接和事务进行测试
```java
@Service("XATransactionService")
public class XATransactionService {

    @Autowired
    @Qualifier("db1DataSource")
    private AtomikosDataSourceBean db1DataSourceBean;

    @Autowired
    @Qualifier("db2DataSource")
    private AtomikosDataSourceBean db2DataSourceBean;

    /**
     * 测试XA事务
     *
     * @param errorSQL
     */
    @Transactional("XATransactionManager")
    public void run(String errorSQL) {
        Connection db1Connection = null;
        Connection db2Connection = null;
        try {
            //test db1
            db1Connection = db1DataSourceBean.getConnection();
            String sql1 = "insert into `demo`(id, `name`) values" +
                    "(?, ?)";
            PreparedStatement db1Statement = db1Connection.prepareStatement(sql1);
            db1Statement.setObject(1, null);
            db1Statement.setObject(2, "ipman");
            db1Statement.execute();

            //test db2
            db2Connection = db2DataSourceBean.getConnection();
            String sql2 = "insert into `demo`(id, `name`) values" +
                    "(?, ?)";
            PreparedStatement db2Statement = db2Connection.prepareStatement(
                    Optional.ofNullable(errorSQL).orElse(sql2));
            db2Statement.setObject(1, null);
            db2Statement.setObject(2, "ipman");
            db2Statement.execute();

            db1Statement.close();
            db2Statement.close();

        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (db1Connection != null) {
                    db1Connection.close();
                }
                if (db2Connection != null) {
                    db2Connection.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
```

##### 4.运行AtomikosFrameApplication查看测试结果


### 四.分布式事务XA总结
![https://raw.githubusercontent.com/ipipman/JavaKnowledge/master/XT/XA%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1.png](https://raw.githubusercontent.com/ipipman/JavaKnowledge/master/XT/XA%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1.png "https://raw.githubusercontent.com/ipipman/JavaKnowledge/master/XT/XA%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1.png")
