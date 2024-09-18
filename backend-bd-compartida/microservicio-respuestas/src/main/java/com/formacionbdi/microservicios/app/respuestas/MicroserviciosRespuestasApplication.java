package com.formacionbdi.microservicios.app.respuestas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// Habilitamos este microservicio como un cliente de un servidor Eureka para que pueda ser descubierto por este servidor y pueda ser registrado(Realmente poner esta anotación es opcional porque si no se pone, como este proyecto incluye la librería "spring-cloud-starter-netflix-eureka-client", automáticamente envía la señal a nuestro servidor Eureka para que se registre, pero está bien indicar esta anotación porque queda más claro y se entiende mejor)
@EnableEurekaClient
@SpringBootApplication
// Como las clases entidad Alumno y Pregunta las importamos de nuestras librerías "commons-alumnos" y "commons-examenes" y se encuentran en paquetes ("com.formacionbdi.microservicios.commons.alumnos.models.entity" y "com.formacionbdi.microservicios.commons.examenes.models.entity") distintos al paquete raíz("com.formacionbdi.microservicios.app.respuestas") de este proyecto Spring Boot, tenemos que añadir la siguiente anotación para que se busquen y se tengan en cuenta las anotaciones JPA de las clases entidad Respuesta, Alumno y Pregunta que se encuentran dentro de esos paquetes
@EntityScan({"com.formacionbdi.microservicios.app.respuestas.models.entity",
	"com.formacionbdi.microservicios.commons.alumnos.models.entity",
	"com.formacionbdi.microservicios.commons.examenes.models.entity"})
public class MicroserviciosRespuestasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosRespuestasApplication.class, args);
	}

}
