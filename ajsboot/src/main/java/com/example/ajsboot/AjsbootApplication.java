package com.example.ajsboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AjsbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(AjsbootApplication.class, args);
	}

}
