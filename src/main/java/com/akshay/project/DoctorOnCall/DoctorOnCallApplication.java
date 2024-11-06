package com.akshay.project.DoctorOnCall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.akshay.project.DoctorOnCall.repository")
@EntityScan(basePackages = "com.akshay.project.DoctorOnCall.entity")
public class DoctorOnCallApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorOnCallApplication.class, args);

	}
}
