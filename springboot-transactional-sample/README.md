# Spring的事务7种传播机制和隔离级别

### Propagation（事务传播属性）
Propagation 属性确定代理应该对那些方法增加事务行为，这样属性最重要的部分是传播行为：
> - PROPAGATION_REQUIRED 支持当前事务，如果当前没有事务，则新建一个事务，这是最常见的选择，也是 Spring 默认的


