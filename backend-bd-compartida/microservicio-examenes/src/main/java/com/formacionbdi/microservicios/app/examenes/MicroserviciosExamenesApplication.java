package com.formacionbdi.microservicios.app.examenes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//Habilitamos este microservicio como un cliente de un servidor Eureka para que pueda ser descubierto por este servidor y pueda ser registrado(Realmente poner esta anotación es opcional porque si no se pone, como este proyecto incluye la librería "spring-cloud-starter-netflix-eureka-client", automáticamente envía la señal a nuestro servidor Eureka para que se registre, pero está bien indicar esta anotación porque queda más claro y se entiende mejor)
@EnableEurekaClient
@SpringBootApplication
//Como las clases entidad Examen y Pregunta las importamos de nuestra librería "commons-examenes" y se encuentran en un paquete("com.formacionbdi.microservicios.commons.examenes.models.entity") distinto al paquete raíz("com.formacionbdi.microservicios.app.examenes") de este proyecto Spring Boot, tenemos que añadir la siguiente anotación para que también se busquen y se tengan en cuenta las anotaciones JPA de las clases entidad que hay dentro de ese paquete
@EntityScan({"com.formacionbdi.microservicios.commons.examenes.models.entity"})
public class MicroserviciosExamenesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosExamenesApplication.class, args);
	}

}
