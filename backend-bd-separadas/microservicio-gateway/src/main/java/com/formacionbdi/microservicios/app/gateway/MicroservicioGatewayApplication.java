package com.formacionbdi.microservicios.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// Habilitamos este API Gateway de Spring Cloud como un cliente de un servidor Eureka para que pueda ser descubierto por este servidor y pueda ser registrado(Realmente poner esta anotación es opcional porque si no se pone, como este proyecto incluye la librería "spring-cloud-starter-netflix-eureka-client", automáticamente envía la señal a nuestro servidor Eureka para que se registre, pero está bien indicar esta anotación porque queda más claro y se entiende mejor)
@EnableEurekaClient
@SpringBootApplication
public class MicroservicioGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioGatewayApplication.class, args);
	}

}
