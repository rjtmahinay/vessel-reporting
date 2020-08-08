package com.karagathon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageConfig implements WebMvcConfigurer {
	
	@Value("${violation.file.path}")
	String destination;

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {

            registry.addResourceHandler("/uploads/**").addResourceLocations("file:///E:/spring/vessel-reporting/ViolationMediaUploads/").setCachePeriod(0);
            System.out.println("Image configuration initialized");
        }
}