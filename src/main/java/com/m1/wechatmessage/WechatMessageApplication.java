package com.m1.wechatmessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WechatMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatMessageApplication.class, args);
	}
}
