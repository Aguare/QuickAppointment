package com.example.app_backend;

import com.example.app_backend.repositories.CompanySettingRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppBackendApplication.class, args);
		System.out.println("AppBackendApplication is running!");
	}

}
