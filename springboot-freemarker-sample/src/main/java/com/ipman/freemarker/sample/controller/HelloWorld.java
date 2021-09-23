package com.ipman.freemarker.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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

}
