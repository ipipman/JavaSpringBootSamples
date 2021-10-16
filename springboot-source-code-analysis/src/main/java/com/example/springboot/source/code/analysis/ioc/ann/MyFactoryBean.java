package com.example.springboot.source.code.analysis.ioc.ann;

import com.example.springboot.source.code.analysis.ioc.pojo.Animal;
import com.example.springboot.source.code.analysis.ioc.pojo.Cat;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * Created by ipipman on 2021/10/16.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.ioc.ann
 * @Description: (通过实现 FactoryBean < ? > 接口实现Bean的注入)
 * @date 2021/10/16 12:15 下午
 */
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
