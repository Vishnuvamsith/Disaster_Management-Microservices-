package com.example.safeplaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class SafeplacesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeplacesApplication.class, args);
	}

}
