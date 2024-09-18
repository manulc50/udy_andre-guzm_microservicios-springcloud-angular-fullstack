package com.formacionbdi.microservicios.app.alumnos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//Habilitamos Feign en este microservicio para que pueda comunicarse con otros microservicios
@EnableFeignClients
//Habilitamos este microservicio como un cliente de un servidor Eureka para que pueda ser descubierto por este servidor y pueda ser registrado(Realmente poner esta anotación es opcional porque si no se pone, como este proyecto incluye la librería "spring-cloud-starter-netflix-eureka-client", automáticamente envía la señal a nuestro servidor Eureka para que se registre, pero está bien indicar esta anotación porque queda más claro y se entiende mejor)
@EnableEurekaClient
@SpringBootApplication
// Como la clase entidad Alumno la importamos de nuestra librería "commons-alumnos" y se encuentra en un paquete("com.formacionbdi.microservicios.commons.alumnos.models.entity") distinto al paquete raíz("com.formacionbdi.microservicios.app.alumnos") de este proyecto Spring Boot, tenemos que añadir la siguiente anotación para que también se busquen y se tengan en cuenta las anotaciones JPA de las clases entidad que hay dentro de ese paquete(solo hay una que es la clase entidad Alumno)
@EntityScan({"com.formacionbdi.microservicios.commons.alumnos.models.entity"})
public class MicroservicioAlumnosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioAlumnosApplication.class, args);
	}

}
