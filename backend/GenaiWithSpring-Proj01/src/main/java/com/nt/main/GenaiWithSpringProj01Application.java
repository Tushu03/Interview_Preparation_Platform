package com.nt.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;





@SpringBootApplication
@ComponentScan(basePackages = "com.nt")
public class GenaiWithSpringProj01Application {

	public static void main(String[] args) {
	SpringApplication.run(GenaiWithSpringProj01Application.class, args);
        
	}
	
	
	@Bean
	public RestTemplate createRest() {
		
		return new RestTemplate();
	}
	

}
