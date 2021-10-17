### 框架Refresh方法解析二



#### 1.配置工厂标准上下文特征：prepareBeanFacotry()

- 作用
  - 设置beanFacotry一些属性
  - 添加后置处理器
  - 设置忽略的自动装配接口
  - 注册一些组件

```java
	/**
	 * Configure the factory's standard context characteristics,
	 * such as the context's ClassLoader and post-processors.
	 * @param beanFactory the BeanFactory to configure
	 * 配置工厂的标准上下文特征，比如上下文的ClassLoader和post-processors
	 */
	protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		// Tell the internal bean factory to use the context's class loader etc.
    // 告知内部的Bean工厂，使用上下文的类加载器
		beanFactory.setBeanClassLoader(getClassLoader());

    // 设置Bean表达式解析器
    // 默认是spring el 表达式
		beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));

    // 注册上下文enviroment配置的引用
    // 用于做属性转换
		beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

		// Configure the bean factory with context callbacks.
    // 使用上下文回调配置 Bean 工厂
    // 在工厂的 beanPostProcessor 属性中添加Bean的后置处理器，beanPostProcessor是一个ArrayList
		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    // 在工厂的忽略依赖接口ignoreDependencyInterface列表中添加Aware系列的接口
    // 以下Aware接口由 ApplicationContextAwareProcessor 后置处理器处理
		beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
		beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
		beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
		beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);

		// BeanFactory interface not registered as resolvable type in a plain factory.
		// MessageSource registered (and found for autowiring) as a bean.
    // 在普通的工厂中，BeanFactory接口并没有按照resolvable类型进行注册
    // MessageSource被注册成一个Bean（并被自动注入）
    
    //BeanFactory.class为key，beanFactory为value放入到了beanFactory的resolvableDependencies属性中
    //resolvableDependencies是一个ConcurrentHashMap,映射依赖类型和对应的被注入的value
    //这样的话BeanFactory/ApplicationContext虽然没有以bean的方式被定义在工厂中，
    //但是也能够支持自动注入，因为他处于resolvableDependencies属性中
    
    // 向BeanFacotry添加一些依赖，需要解析依赖的时候指向自身
		beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
    // 再将上下文的一些接口与上下文本身做映射，一一放入到resolvableDependencies中
		beanFactory.registerResolvableDependency(ResourceLoader.class, this);
		beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
		beanFactory.registerResolvableDependency(ApplicationContext.class, this);

		// Register early post-processor for detecting inner beans as ApplicationListeners.
    // 将用于检测内部 bean 的早期后处理器注册为 ApplicationListeners
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

    // Detect a LoadTimeWeaver and prepare for weaving, if found.
    // 检测LoadTimeWeaver，如果有就准备织入
		if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
      // 如果有LoadTimeWeaver，加入bean后处理器
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
      // Set a temporary ClassLoader for type matching.
      // 为匹配类型设置一个临时的ClassLoader
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}

		// Register default environment beans.
    // 注册默认的environment beans
		if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
      //虽然XmlWebApplicationContext中持有默认实现的StandardServletEnvironment
      //但是没有注册到beanFactory中，通过getEnvironment方法拿到持有的引用
      //2.注册environment单例
			beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
      //注册systemProperties单例
			beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
      ///注册systemEnvironment单例
			beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
		}
	}
```



#### 2.重写BeanFactory，在BeanFactory创建后进一步设置：postProcessBeanFactory()

- 作用是
  - 留给开发者的扩展点，通过子类重写，在BeanFactory完成创建后做进一步设置
  - 如添加WebApplicationContextServletContextAwareProcessor后置处理器，给Web环境下的Servlet作用域进行设置：Request、Session

- postProcessBeanFacotory()方法

```java
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
       	// 调用父类的GenericWebApplicationContext#postProcessBeanFacotory方法
        super.postProcessBeanFactory(beanFactory);
        if (!ObjectUtils.isEmpty(this.basePackages)) {
            this.scanner.scan(this.basePackages);
        }

        if (!this.annotatedClasses.isEmpty()) {
            this.reader.register(ClassUtils.toClassArray(this.annotatedClasses));
        }

    }
```

- 在Web环境下注册WebApplicationContextServletContextAwareProcessor后置处理器

```java
	/**
	 * Register ServletContextAwareProcessor.
	 * @see ServletContextAwareProcessor
	 */
	@Override
	protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    // 添加WebApplicationContextServletContextAwareProcessor后置处理器
		beanFactory.addBeanPostProcessor(new WebApplicationContextServletContextAwareProcessor(this));
    // 忽略ServletContextAware
		beanFactory.ignoreDependencyInterface(ServletContextAware.class);
    // 定义Web环境下的Servlet作用域，比如：Request、Session
		registerWebApplicationScopes();
	}

```





#### 3.调用所有注册的BeanFactoryPostProcessor的Bean

- 作用

  - 框架自身的BeanFactoryPostProcessor或BeanDefinitionRegistoryPostProcessor初始化Bean的定义或属性

  - 调用BeanDefinitionRegistryPostProcessor实现向容器内添加Bean的定义

    - ```java
      // 调用时机：在BeanFactory标准初始化之后调用，这时所有的Bean定义已经保存加载到BeanFactory，但是Bean的实例还未创建
      // 作用：来定制和修改BeanFactory内容，如添加一个Bean
      @Component
      public class MyBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {
      
          /**
           * 通过BeanDefinitionRegistryPostProcessor
           */
          @Override
          public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
              RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
              rootBeanDefinition.setBeanClass(Monkey.class);
              beanDefinitionRegistry.registerBeanDefinition("monkey", rootBeanDefinition);
          }
      
          @Override
          public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
      
          }
      }
      ```

  - 调用BeanFacotryPostProcessor实现容器内Bean的定义添加属性

    - ```java
      // 调用时机：在BeanFactory标准初始化之后调用，这时所有的Bean定义已经保存加载到BeanFactory，但是Bean的实例还未创建
      // 作用：来定制和修改BeanFactory内容，如覆盖或添加属性
      @Component
      public class MyBeanFacotoryPostProcessor implements BeanFactoryPostProcessor {
      
          @Override
          public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
              // 给DemoBean添加属性
              BeanDefinition beanDemo = configurableListableBeanFactory.getBeanDefinition("demoBean");
              MutablePropertyValues propertyValues = beanDemo.getPropertyValues();
              propertyValues.addPropertyValue("name", "ipman");
          }
      }
      ```

- 步骤

  - 步骤一

    <img src="https://tva1.sinaimg.cn/large/008i3skNly1gvijv8zdrlj61m20u0ad602.jpg" alt="image-20211017195022460" width="700" align="left" />

    

  - 步骤二

    <img src="https://tva1.sinaimg.cn/large/008i3skNly1gvijyl6ea7j613q0u041h02.jpg" alt="image-20211017195333372" width="700" align="left" />

    

  - 步骤三

    <img src="https://tva1.sinaimg.cn/large/008i3skNly1gvik0b2p0kj615g0u0who02.jpg" alt="image-20211017195520176" width="700"  align="left" />

    

  - 步骤四

    <img src="https://tva1.sinaimg.cn/large/008i3skNly1gvik2kuxirj60kw11owhd02.jpg" alt="image-20211017195711612" align="left" width="400" height="500" />

- invokeBeanFacotoryPostProcessors()方法

```java
public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

        // 保存所有调用过的PostProcessor的beanName
        Set<String> processedBeans = new HashSet<>();

        if (beanFactory instanceof BeanDefinitionRegistry) {
                BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
                // 这两个list主要用来分别收集BeanFactoryPostProcessor和BeanDefinitionRegistryPostProcessor
                List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
                List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();

                for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
                        // 对已注册在DefaultListBeanFactory的BeanFactoryPostProcessor进行分类
                        if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
                                BeanDefinitionRegistryPostProcessor registryProcessor =
                                                (BeanDefinitionRegistryPostProcessor) postProcessor;
                                // 已注册在DefaultListBeanFactory的BeanDefinitionRegistryPostProcessor优先级最高
                                // 如果postProcessor是BeanDefinitionRegistryPostProcessor的实例
                                // 执行postProcessor的postProcessBeanDefinitionRegistry
                                // 为什么执行完之后还要保存到List中呢？
                                // 因为这里只是执行完了BeanDefinitionRegistryPostProcessor的回调
                                // 父类BeanFactoryPostProcessor的方法还没有进行回调
                                registryProcessor.postProcessBeanDefinitionRegistry(registry);
                                registryProcessors.add(registryProcessor);
                        }
                        else {
                              // 如果不是BeanDefinitionRegistryPostProcessor的实例
                              // 则是BeanFactoryPostProcessor的实例，存放在List中，后面会进行回调
                              regularPostProcessors.add(postProcessor);
                        }
                }

                // 定义了一个集合来存放当前需要执行的BeanDefinitionRegistryPostProcessor
                List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

                // 首先执行实现了PriorityOrdered的BeanDefinitionRegistryPostProcessor
                // 这里只能获取到Spring内部注册的BeanDefinitionRegistryPostProcessor，因为到这里spring还没有去扫描Bean，获取不到我们               								// 通过@Component标志的自定义的BeanDefinitionRegistryPostProcessor
                // 一般默认情况下,这里只有一个beanName,
                // org.springframework.context.annotation.internalConfigurationAnnotationProcessor
                // 对应的BeanClass：ConfigurationClassPostProcessor
                String[] postProcessorNames =
                                beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
                for (String ppName : postProcessorNames) {
                        if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                                currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                                // 保存调用过的beanName
                                processedBeans.add(ppName);
                        }
                }
                // 排序
                sortPostProcessors(currentRegistryProcessors, beanFactory);
                // registryProcessors存放的是BeanDefinitionRegistryPostProcessor
                registryProcessors.addAll(currentRegistryProcessors);
                // 执行BeanDefinitionRegistryPostProcessor，一般默认情况下，只有ConfigurationClassPostProcessor
                // ConfigurationClassPostProcessor的具体作用后面再讲，这里先认为它执行完扫描，并且注册BeanDefinition
                invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
                // 清空临时变量，后面再使用
                currentRegistryProcessors.clear();

                // 第二步执行实现了Ordered的BeanDefinitionRegistryPostProcessor
                // 这里为什么要再一次从beanFactory中获取所有的BeanDefinitionRegistryPostProcessor，是因为上面的操作有可能注册了
                // 新的BeanDefinitionRegistryPostProcessor，所以再获取一次
                postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
                for (String ppName : postProcessorNames) {
                        if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
                                currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                                // 保存调用过的beanName
                                processedBeans.add(ppName);
                        }
                }
                sortPostProcessors(currentRegistryProcessors, beanFactory);
                registryProcessors.addAll(currentRegistryProcessors);
                invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
                currentRegistryProcessors.clear();


                // 上面两步的套路是一模一样的，唯一不一样的地方是第一步执行的是实现了PriorityOrdered的
                // BeanDefinitionRegistryPostProcessor，第二步是执行的是实现了Ordered的
                // BeanDefinitionRegistryPostProcessor


                // 最后一步，执行没有实现PriorityOrdered或者Ordered的BeanDefinitionRegistryPostProcessor
                // 比较不一样的是，这里有个while循环，是因为在实现BeanDefinitionRegistryPostProcessor的方法的过程中有可能会注册新的
                // BeanDefinitionRegistryPostProcessor，所以需要处理，直到不会出现新的BeanDefinitionRegistryPostProcessor为止
                boolean reiterate = true;
                while (reiterate) {
                        reiterate = false;
                        postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
                        for (String ppName : postProcessorNames) {
                                if (!processedBeans.contains(ppName)) {
                                        // 发现还有未处理过的BeanDefinitionRegistryPostProcessor，按照套路放进list中
                                        // reiterate标记为true，后面还要再执行一次这个循环，因为执行新的BeanDefinitionRegistryPostProcessor有可能会
                                        // 注册新的BeanDefinitionRegistryPostProcessor
                                        currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                                        processedBeans.add(ppName);
                                        reiterate = true;
                                }
                        }
                        sortPostProcessors(currentRegistryProcessors, beanFactory);
                        registryProcessors.addAll(currentRegistryProcessors);
                        invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
                        currentRegistryProcessors.clear();
                }
                // 方法开头前几行分类的两个list就是在这里调用
                // BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
                // 刚刚执行了BeanDefinitionRegistryPostProcessor的方法
                // 现在要执行父类BeanFactoryPostProcessor的方法
                invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
                invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
        }

        else {
                // Invoke factory processors registered with the context instance.
                invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
        }


        // 上面BeanFactoryPostProcessor的回调可能又注册了一些类，下面需要再走一遍之前的逻辑
        String[] postProcessorNames =
                        beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

        // 根据不同的优先级，分成三类
        List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
        List<String> orderedPostProcessorNames = new ArrayList<>();
        List<String> nonOrderedPostProcessorNames = new ArrayList<>();
        for (String ppName : postProcessorNames) {
                // 上面处理BeanDefinitionRegistryPostProcessor的时候已经处理过，这里不再重复处理
                // 因为BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
                if (processedBeans.contains(ppName)) {
                        // skip - already processed in first phase above
                }
                else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                        priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
                }
                else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
                        orderedPostProcessorNames.add(ppName);
                }
                else {
                        nonOrderedPostProcessorNames.add(ppName);
                }
        }

        // First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
        // 下面的逻辑跟上面是一致的，先处理实现了PriorityOrdered接口的
        // 再处理实现了Ordered接口的
        // 最后处理普通的BeanFactoryPostProcessor
        sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
        invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

        // Next, invoke the BeanFactoryPostProcessors that implement Ordered.
        List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>();
        for (String postProcessorName : orderedPostProcessorNames) {
                orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
        }
        sortPostProcessors(orderedPostProcessors, beanFactory);
        invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

        // Finally, invoke all other BeanFactoryPostProcessors.
        List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
        for (String postProcessorName : nonOrderedPostProcessorNames) {
                nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
        }
        invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

        // 因为各种BeanFactoryPostProcessor可能修改了BeanDefinition
        // 所以这里需要清除缓存，需要的时候再通过merge的方式获取
        beanFactory.clearMetadataCache();
}
```

