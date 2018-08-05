package com.me.house;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.me4test.house.autoconfig.EnableHttpClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableHttpClient
@EnableAsync
public class HouseApplication {
	public static void main(String[] args) {
		SpringApplication.run(HouseApplication.class, args);
	}
}
