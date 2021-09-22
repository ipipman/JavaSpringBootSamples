package com.example.springboot.source.code.analysis.xml;

import java.util.List;

/**
 * Created by ipipman on 2021/9/22.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.xml
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/9/22 10:19 上午
 */
public class Student {

    private String name;
    private Integer age;
    private List<String> classList;

    /**
     * 使用构造器注入
     *
     * @param name
     * @param age
     */
    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", classList=" + String.join(",", classList) +
                '}';
    }
}
