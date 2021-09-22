package com.example.springboot.source.code.analysis.xml;

/**
 * Created by ipipman on 2021/9/22.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.xml
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/9/22 10:26 上午
 */
public class HelloService {

    private Student student;

    private Animal animal;

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String hello() {
        //return student.toString();
        return animal.getName();
    }

}
