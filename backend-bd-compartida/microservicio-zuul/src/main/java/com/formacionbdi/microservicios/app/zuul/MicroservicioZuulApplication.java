package com.formacionbdi.microservicios.app.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

// Habilitamos el proxy Zuul para que haga de API Gateway(puerta de enlace) para nuestros microservicios 
@EnableZuulProxy
// Habilitamos este API Gateway Zuul como un cliente de un servidor Eureka para que pueda ser descubierto por este servidor y pueda ser registrado(Realmente poner esta anotación es opcional porque si no se pone, como este proyecto incluye la librería "spring-cloud-starter-netflix-eureka-client", automáticamente envía la señal a nuestro servidor Eureka para que se registre, pero está bien indicar esta anotación porque queda más claro y se entiende mejor)
@EnableEurekaClient
@SpringBootApplication
public class MicroservicioZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioZuulApplication.class, args);
	}

}
