package com.ipman.springcloud.consul.ribbon.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringcloudConsulRibbonSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudConsulRibbonSampleApplication.class, args);
	}

}
