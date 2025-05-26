package com.grupo_exito.microservicio_tarjetas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class MicroservicioTarjetasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioTarjetasApplication.class, args);
	}

}
