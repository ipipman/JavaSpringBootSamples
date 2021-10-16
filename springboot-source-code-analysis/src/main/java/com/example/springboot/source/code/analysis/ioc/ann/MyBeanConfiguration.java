package com.example.springboot.source.code.analysis.ioc.ann;

import com.example.springboot.source.code.analysis.ioc.pojo.Animal;
import com.example.springboot.source.code.analysis.ioc.pojo.Dog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ipipman on 2021/10/16.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.ioc.ann
 * @Description: (通过 @Configuration注解 注入一个Bean)
 * @date 2021/10/16 12:04 下午
 */
@Configuration
public class MyBeanConfiguration {

    @Bean("dog")
    public Animal getDog() {
        return new Dog();
    }

}
