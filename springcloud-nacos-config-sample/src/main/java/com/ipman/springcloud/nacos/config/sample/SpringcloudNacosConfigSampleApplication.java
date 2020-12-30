package com.ipman.springcloud.nacos.config.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringcloudNacosConfigSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudNacosConfigSampleApplication.class, args);
	}

}
