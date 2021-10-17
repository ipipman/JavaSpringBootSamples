package com.example.springboot.source.code.analysis.ioc.ann;

import com.example.springboot.source.code.analysis.ioc.pojo.Monkey;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by ipipman on 2021/10/16.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.ioc.ann
 * @Description: (通过BeanDefinitionRegistryPostProcessor 进行Bean的注入)
 * @date 2021/10/16 3:18 下午
 */
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
