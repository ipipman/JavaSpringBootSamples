package com.example.springboot.source.code.analysis.ioc.service;

import com.example.springboot.source.code.analysis.ioc.pojo.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.swing.event.AncestorEvent;

/**
 * Created by ipipman on 2021/10/16.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.ioc.service
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/10/16 12:10 下午
 */

@Component
public class HelloService {

    /**
     * animal
     */
    @Autowired
    @Qualifier("myCatFactoryBean") // 如果有多个同类型的Bean，那么用@Qualifier指定Bean的名称后进行注入
    private Animal animal;


    public String hello() {
        return animal.getName();
    }
}
