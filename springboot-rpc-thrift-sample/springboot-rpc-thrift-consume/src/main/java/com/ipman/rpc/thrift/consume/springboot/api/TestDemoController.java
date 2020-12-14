package com.ipman.rpc.thrift.consume.springboot.api;

import com.ipman.rpc.thrift.consume.springboot.pojo.DemoPOJO;
import com.ipman.rpc.thrift.consume.springboot.service.TestDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ipipman on 2020/12/14.
 *
 * @version V1.0
 * @Package com.ipman.rpc.thrift.consume.springboot.api
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/12/14 5:58 下午
 */
@RestController
@RequestMapping("test")
public class TestDemoController {

    @Autowired
    TestDemoService testDemoService;

    @GetMapping("/get")
    public Object getDemo(String name) {
        return testDemoService.getDemoItem(name);
    }

    @GetMapping("/save")
    public void saveDemo(DemoPOJO demoPOJO) {
        testDemoService.save(demoPOJO);
    }
}
