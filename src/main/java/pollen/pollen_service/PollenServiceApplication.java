package pollen.pollen_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PollenServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollenServiceApplication.class, args);
	}

}
