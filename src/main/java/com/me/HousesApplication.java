package com.me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import test.com.me.autoconfig.EnableHttpClient;

@SpringBootApplication
@EnableHttpClient
public class HousesApplication {
	public static void main(String[] args) {
		SpringApplication.run(HousesApplication.class, args);
	}
}
