# Spring的事务7种传播机制和隔离级别

### Propagation（事务传播属性）
Propagation 属性确定代理应该对那些方法增加事务行为，这样属性最重要的部分是传播行为：
> - **PROPAGATION_REQUIRED**  ----> 支持当前事务，如果当前没有事务，则新建一个事务，这是最常见的选择，也是 Spring 默认的一个事务传播行为；
> - **PROPAGATION_SUPPORTS** ----> 支持当前事务，如果当前没有事务，则以非事务的方式执行；
> - **PROPAGATION_MANDATORY** ----> 支持当前事务，如果当前没有事务，则抛出异常；
> - **PROPAGATION_REQUIRES_NEW** ----> 新建事务，如果当前存在事务，把当前事务挂起；
> - **PROPAGATION_NOT_SUPPORTED** ----> 以非事务的方式执行操作，如果当前存在事务，就把当前事务挂起；
> - **PROPAGATION_NEVER** ----> 以非事务方式执行，如果当前存在事务，则抛出异常；
> - **PROPAGATION_NESTED** ----> Nested的事务和它的父事务是相依的，它的提交是等它的父事务一块提交的；


#### （一）PROPAGATION_REQUIRED
注解用法比如：@Transactional（propagation = Propagation.REQUIRED）
默认的 spring 事务传播级别，使用该级别的特点是：如果上下文中已经存在事务，那么就加入到事务中执行，如果当前上下文不存在事务，则新建事务执行
在大多数业务场景下通常都能满足

#### （二）PROPAGATION_SUPPORTS
该传播级别的特点是：如果上下文存在事务，则支持事务加入事务，如果没有事务，则使用非事务的方式执行。所以说，并非所有的包在transactionTemplate.execute中的代码都会有事务支持。这个通常是用来处理那些非原子性的非核心业务逻辑操作。
应用场景较少

#### （三）PROPAGATION_MANDATORY
该级别的事务要求上下文中必须要存在事务，否则就会抛出异常
配置改方法的传播级别是有效的控制上下文调用代码遗漏添加事务控制的保证手段
比如一段代码不能单独被调用执行，但是一旦被调用，就必须有事务包含的情况，就可以使用这个传播级别


