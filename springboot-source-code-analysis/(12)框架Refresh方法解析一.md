

### 框架Refresh方法解析一



#### 1.Refresh方法简介

- Bean配置读取加载入口
- Spring框架的启动流程



#### 2.Refresh方法的执行步骤

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gvib55enehj61eg0u041602.jpg" alt="image-20211017144834689" align="left" width="800" />

```java
	@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
      // 准备此上下文
			prepareRefresh();

			// Tell the subclass to refresh the internal bean factory.
      // 获取BeanFacotry
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
      // 准备此类上下文中使用的Bean工厂
			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean factory in context subclasses.
        // 允许在上下文子类中对Bean工厂进行后处理
				postProcessBeanFactory(beanFactory);

				// Invoke factory processors registered as beans in the context.
        // 调用在上下文中注册为Bean的工厂处理器
				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
        // 注册拦截Bean创建的Bean处理器
				registerBeanPostProcessors(beanFactory);

				// Initialize message source for this context.
        // 为此上下文初始化消息源
				initMessageSource();

				// Initialize event multicaster for this context.
        // 为此上下文初始化事件广播器
				initApplicationEventMulticaster();

				// Initialize other special beans in specific context subclasses.
        // 初始化特定上下文子类中的其他特殊Bean
				onRefresh();

				// Check for listener beans and register them.
        // 检查监听器，并注册
				registerListeners();

				// Instantiate all remaining (non-lazy-init) singletons.
        // 实例化所有剩余的（非延迟初始化）单例。
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
        // 最后一步：发布相应的事件
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
        // 销毁已经创建的单例以避免浪费资源
				destroyBeans();

				// Reset 'active' flag.
        // 重置active标志
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
       	// 重置 Spring 核心中的常见内省缓存，因为我们可能不再需要单例 bean 的元数据...
				resetCommonCaches();
			}
		}
	}
```



#### 3.准备上下文：prepareRefersh#方法

- prepareRefersh#方法简介
  - 清除本地元数据缓存
  - 准备框架上下文
    - 设置框架启动时间
    - 设置框架 active 启动状态为true
    - 如果是Debug模式打印Debug日志
    - 初始化环境配置
      - 获取当前环境配置
      - 如果是Web环境，配置ServletContext 和 ServletConfig
    - 检查环境配置中的必备属性是否存在
    - 如果没有早期的事件监听器，就注册应用监听器：applicationListeners

-  prepareRefresh#方法

```java
	@Override
	protected void prepareRefresh() {
    // 清除元数据缓存
		this.scanner.clearCache();
    // 准备上下文
		super.prepareRefresh();
	}
```

- clearCache#方法

```java
	/**
	 * Clear the local metadata cache, if any, removing all cached class metadata.
	 * 清除本地元数据缓存（如果有），删除所有缓存的类元数据。
	 */
	public void clearCache() {
		if (this.metadataReaderFactory instanceof CachingMetadataReaderFactory) {
			// Clear cache in externally provided MetadataReaderFactory; this is a no-op
			// for a shared cache since it'll be cleared by the ApplicationContext.
      // 清除外部提供的 MetadataReaderFactory 中的缓存；这是一个无操作用于共享缓存，因为它将被 ApplicationContext 清除。
			((CachingMetadataReaderFactory) this.metadataReaderFactory).clearCache();
		}
	}
```

-  super.prepareRefresh方法

```java
	/**
	 * Prepare this context for refreshing, setting its startup date and
	 * active flag as well as performing any initialization of property sources.
	 * 准备此上下文以进行刷新、设置其启动日期和* active 标志以及执行属性源的任何初始化
	 */
	protected void prepareRefresh() {
		// Switch to active.
    // 设置启动时间
		this.startupDate = System.currentTimeMillis();
		this.closed.set(false);
    // 设置 active 标志状态为 true
		this.active.set(true);
    
    // 如果当前日志状态是Debug模式的话，会打印一段话
		if (logger.isDebugEnabled()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Refreshing " + this);
			}
			else {
				logger.debug("Refreshing " + getDisplayName());
			}
		}

		// Initialize any placeholder property sources in the context environment.
    // 初始化环境配置
		initPropertySources();

		// Validate that all properties marked as required are resolvable:
		// see ConfigurablePropertyResolver#setRequiredProperties
    // 检查环境属性中必备的环境数据，如果没有就 throw MissingRequiredPropertiesException 异常
		getEnvironment().validateRequiredProperties();

		// Store pre-refresh ApplicationListeners...
    // 将系统的事件监听器进行注册
		if (this.earlyApplicationListeners == null) {
			this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
		}
		else {
			// Reset local application listeners to pre-refresh state.
			this.applicationListeners.clear();
			this.applicationListeners.addAll(this.earlyApplicationListeners);
		}

    // Allow for the collection of early ApplicationEvents,
		// to be published once the multicaster is available...
		this.earlyApplicationEvents = new LinkedHashSet<>();
	}

```

- initPropertySources#方法

```java
    protected void initPropertySources() {
        // 获取环境配置上下文
        ConfigurableEnvironment env = this.getEnvironment();
     		// 如果当前环境是Web环境，将servletContext 和 ServletConfig 加载到环境配置上下文中
        if (env instanceof ConfigurableWebEnvironment) {
            ((ConfigurableWebEnvironment)env).initPropertySources(this.servletContext, (ServletConfig)null);
        }
    }
```





#### 获取新的Bean工厂：obtainFreshBeanFacotry#方法

- obtainFreshBeanFacotry#方法简介
  - 设置当前上下文刷新状态为true
  - 这是BeanFactory序列化ID（默认是application）
  - 返回BeanFacotry

- obtainFreshBeanFactory#方法

```java
	/**
	 * Tell the subclass to refresh the internal bean factory.
	 * 告诉子类刷新内部 bean 工厂
	 * @return the fresh BeanFactory instance
	 * @see #refreshBeanFactory()
	 * @see #getBeanFactory()
	 */
	protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
    // 设置上下文刷新状态和Beanfacotory的序列化ID
		refreshBeanFactory();
    // 获取BeanFactory（默认是DefaultListableBeanFacotry）
		return getBeanFactory();
	}

```

- refreshBeanFacoty#方法

```java
	/**
	 * Do nothing: We hold a single internal BeanFactory and rely on callers
	 * to register beans through our public methods (or the BeanFactory's).
	 * @see #registerBeanDefinition
	 */
	@Override
	protected final void refreshBeanFactory() throws IllegalStateException {
    // 设置当前上下文刷新状态：true
		if (!this.refreshed.compareAndSet(false, true)) {
			throw new IllegalStateException(
					"GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
		}
    // 设置BeanFacotry序列化ID：默认是application
		this.beanFactory.setSerializationId(getId());
	}
```

- getBeanFacotory#方法

```java
/**
	 * Return the single internal BeanFactory held by this context
	 * (as ConfigurableListableBeanFactory).
	 */
	@Override
	public final ConfigurableListableBeanFactory getBeanFactory() {
    // 返回默认的 DefaultListableBeanFacotry
		return this.beanFactory;
	}
```



