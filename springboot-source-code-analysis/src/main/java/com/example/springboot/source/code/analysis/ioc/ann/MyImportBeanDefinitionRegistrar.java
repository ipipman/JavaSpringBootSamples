package com.example.springboot.source.code.analysis.ioc.ann;

import com.example.springboot.source.code.analysis.ioc.pojo.Bird;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by ipipman on 2021/10/16.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.ioc.ann
 * @Description: (通过ImportBeanDefinitionRegistrar注入Bean)
 * @date 2021/10/16 3:32 下午
 */
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
