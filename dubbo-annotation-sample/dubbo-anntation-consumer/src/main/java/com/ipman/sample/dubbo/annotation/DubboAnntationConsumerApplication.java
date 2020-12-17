package com.ipman.sample.dubbo.annotation;

import com.ipman.sample.dubbo.annotation.config.ConsumerConfiguration;
import com.ipman.sample.dubbo.annotation.action.AnnotationAction;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class DubboAnntationConsumerApplication {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
		context.start();

		//Consumer Testing
		final AnnotationAction annotationAction = (AnnotationAction) context.getBean("annotationAction");
		System.out.println(annotationAction.doSayHello());
	}

}
