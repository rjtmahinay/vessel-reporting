package com.karagathon.config;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

@Configuration
public class LocaleConfig {
	@PostConstruct
	public void init() {

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		// Check if what date is printing
		System.out.println("Date in UTC: " + new Date().toString());
	}
}
