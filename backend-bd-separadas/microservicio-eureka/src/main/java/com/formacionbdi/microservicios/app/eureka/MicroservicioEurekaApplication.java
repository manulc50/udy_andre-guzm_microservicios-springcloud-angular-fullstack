package com.formacionbdi.microservicios.app.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer // Habilitamos el servidor Eureka
@SpringBootApplication
public class MicroservicioEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioEurekaApplication.class, args);
	}

}
