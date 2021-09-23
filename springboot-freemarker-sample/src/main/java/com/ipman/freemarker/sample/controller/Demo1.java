package com.ipman.freemarker.sample.controller;

import com.ipman.freemarker.sample.entity.Student;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ipipman on 2021/9/23.
 *
 * @version V1.0
 * @Package com.ipman.freemarker.sample.controller
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/9/23 6:17 下午
 */
@RestController
public class Demo1 {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 根据Freemarker模版渲染引擎，渲染HTML代码
     */
    @GetMapping("/demo2")
    public String demo2() throws IOException, TemplateException {
        List<Student> list = new ArrayList<>();
        Student s1 = new Student();
        s1.setIdNo("No1");
        s1.setName("ipman");
        list.add(s1);
        Student s2 = new Student();
        s2.setIdNo("No2");
        s2.setName("ipipman");
        list.add(s2);
        Map<String, Object> param = new HashMap<>();
        param.put("students", list);
        // 根据Freemarker模版渲染引擎，渲染HTML代码
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("demo1.ftl");
        return "html=[" + FreeMarkerTemplateUtils.processTemplateIntoString(template, param) + "]";
    }
}
