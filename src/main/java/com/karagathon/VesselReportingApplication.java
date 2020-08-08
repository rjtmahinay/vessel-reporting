package com.karagathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VesselReportingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VesselReportingApplication.class, args);
	}

}
