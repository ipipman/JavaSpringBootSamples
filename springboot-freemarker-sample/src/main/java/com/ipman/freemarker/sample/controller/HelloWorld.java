package com.ipman.freemarker.sample.controller;

import com.ipman.freemarker.sample.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ipipman
 * @version V1.0
 * @date 2021/9/23
 * @Package com.ipman.freemarker.sample.controller
 * @Description: (Freemarker)
 * @date 2021/9/23 5:28 下午
 */
@Controller
public class HelloWorld {

    /**
     * 测试Freemarker模版
     */
    @GetMapping("/hello")
    public ModelAndView hello() {
        ModelAndView model = new ModelAndView("hello");
        model.addObject("userName", "ipman");
        // 返回模板名称
        return model;
    }

    /**
     * 测试Freemarker对象与列表渲染
     */
    @GetMapping("/hello1")
    public ModelAndView hello1() {
        ModelAndView model = new ModelAndView("hello1");
        List<Student> list = new ArrayList<>();
        Student s1 = new Student();
        s1.setIdNo("No1");
        s1.setName("ipman");
        list.add(s1);
        Student s2 = new Student();
        s2.setIdNo("No2");
        s2.setName("ipipman");
        list.add(s2);
        model.addObject("students", list);
        return model;
    }

}
