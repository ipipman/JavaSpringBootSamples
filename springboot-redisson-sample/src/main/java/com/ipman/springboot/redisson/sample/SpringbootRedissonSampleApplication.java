package com.ipman.springboot.redisson.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SpringbootRedissonSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRedissonSampleApplication.class, args);
	}

}
