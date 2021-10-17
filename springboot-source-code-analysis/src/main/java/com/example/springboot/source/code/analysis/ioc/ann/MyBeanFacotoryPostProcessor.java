package com.example.springboot.source.code.analysis.ioc.ann;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * Created by ipipman on 2021/10/17.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.ioc.ann
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/10/17 6:47 下午
 */

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
