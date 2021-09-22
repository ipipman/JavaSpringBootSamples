package com.example.springboot.source.code.analysis.xml;

/**
 * Created by ipipman on 2021/9/22.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.xml
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/9/22 10:44 上午
 */
public class AnimalFactory {

    // 静态工厂
    public static Animal getAnimal(String type) {
        if ("Dog".equals(type)) {
            return new Dog();
        } else {
            return new Cat();
        }
    }

    // 实例工厂
    public Animal getAnimal1(String type) {
        if ("Dog".equals(type)) {
            return new Dog();
        } else {
            return new Cat();
        }
    }
}
