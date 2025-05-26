package com.grupo_exito.microservicio_apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroservicioApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioApigatewayApplication.class, args);
	}

}
