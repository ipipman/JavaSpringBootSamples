package com.example.springboot.source.code.analysis.ioc.pojo;

import org.springframework.stereotype.Component;

/**
 * Created by ipipman on 2021/10/17.
 *
 * @version V1.0
 * @Package com.example.springboot.source.code.analysis.ioc.pojo
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/10/17 6:53 下午
 */
@Component
public class DemoBean {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
