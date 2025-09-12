package com.example.AirlineManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class AirlineManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineManagementApplication.class, args);
	}

}
