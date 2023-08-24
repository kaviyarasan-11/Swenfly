package com.datamerge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

@ComponentScan(basePackages = {"com.datamerge"})
public class DataMergeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataMergeApplication.class, args);
	}

}
