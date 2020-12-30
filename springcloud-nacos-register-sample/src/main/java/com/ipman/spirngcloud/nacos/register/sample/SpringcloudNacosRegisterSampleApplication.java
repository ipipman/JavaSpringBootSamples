package com.ipman.spirngcloud.nacos.register.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringcloudNacosRegisterSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudNacosRegisterSampleApplication.class, args);
	}
}
