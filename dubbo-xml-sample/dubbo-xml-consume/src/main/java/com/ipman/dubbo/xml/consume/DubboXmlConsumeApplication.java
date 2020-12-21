package com.ipman.dubbo.xml.consume;

import com.ipman.dubbo.xml.consume.action.CallHelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:consumer/dubbo-xml-reference.xml")
public class DubboXmlConsumeApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(DubboXmlConsumeApplication.class, args);

		//Testing
		CallHelloService callHelloService = (CallHelloService) applicationContext.getBean("callHelloService");
		callHelloService.doSayHello();
	}

}
