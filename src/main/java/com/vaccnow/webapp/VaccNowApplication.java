package com.vaccnow.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VaccNowApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccNowApplication.class, args);
	}

}
