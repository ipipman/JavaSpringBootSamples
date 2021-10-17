### 通过Java配置Bean的几种方式

#### 1.通过@Configuration注解注入Bean的方式

```java
@Configuration
public class MyBeanConfiguration {

    /**
     * 通过 @Configuration注解 注入一个Bean
     */
    @Bean("dog")
    public Animal getDog() {
        return new Dog();
    }

}
```



#### 2.通过实现FactoryBean<?>接口的方式注入Bean

```java
@Component
public class MyFactoryBean implements FactoryBean<Animal> {

    /**
     * 返回要注入的类 
     */
    @Override
    public Animal getObject() throws Exception {
        return new Cat();
    }

    /**
     * 获取要注入类的类型 
     */
    @Override
    public Class<?> getObjectType() {
        return Animal.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
    
}
```



#### 3.通过实现BeanDefinitionRegistryPostProcessor接口的方式注入Bean

```java
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



#### 4.通过实现ImportBeanDefinitionRegistrar接口的方式注入Bean

```java
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * 通过ImportBeanDefinitionRegistrar注入Bean
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(Bird.class);
        registry.registerBeanDefinition("bird", rootBeanDefinition);
    }
}

```

> 使用时：用 @Import(MyImportBeanDefinitionRegistrar.class) 进行注入

