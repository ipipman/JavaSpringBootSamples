package com.ipman.springboot.redis.jedis.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.processing.SupportedSourceVersion;

@SpringBootApplication
public class SpringbootJedisSampleApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringbootJedisSampleApplication.class, args);
	}

}
