package com.ubitricity.challenge.carparkubi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class CarparkUbiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarparkUbiApplication.class, args);
	}

}
