package com.ipman.springcloud.consul.register.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient //让注册中心进行服务发现，将服务注册到服务组件上
public class SpringcloudConsulRegisterSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudConsulRegisterSampleApplication.class, args);
	}
}
