package com.project.cruit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CruitApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application-dev.yml,"
			+ "classpath:aws.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(CruitApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}

}
