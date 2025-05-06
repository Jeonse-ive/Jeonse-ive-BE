package com.ayu.realty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RealtyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealtyApplication.class, args);
	}

}
