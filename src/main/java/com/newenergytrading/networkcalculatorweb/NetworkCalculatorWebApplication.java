package com.newenergytrading.networkcalculatorweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
@ComponentScan
@SpringBootApplication
public class NetworkCalculatorWebApplication extends SpringBootServletInitializer implements WebMvcConfigurer {

	@Override
	protected SpringApplicationBuilder configure (SpringApplicationBuilder builder) {
		return builder.sources(NetworkCalculatorWebApplication.class);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("resources/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}

	public static void main(String[] args) {
		SpringApplication.run(NetworkCalculatorWebApplication.class, args);
	}

}
