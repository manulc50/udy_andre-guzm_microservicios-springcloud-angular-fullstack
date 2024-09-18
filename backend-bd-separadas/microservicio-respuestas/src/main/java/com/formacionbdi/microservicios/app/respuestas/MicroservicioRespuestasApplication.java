package com.formacionbdi.microservicios.app.respuestas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// Como este microservicio utiliza una base de datos MongoDB, que es de tipo NoSQL y no usa JPA, y como este proyecto  tiene una dependencia con la librería "spring-data-jpa", tenemos que deshabilitar la autoconfiguración de Spring Boot para que no configure el DataSource de manera automática y así evitar el error "Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured." en la ejecución de esta aplicación
// La dependencia con "spring-data-jpa" que tiene este proyecto es porque estamos incluyendo las dependencias con nuestras librerías "commons-alumnos" y "commons-examenes" que sí utilizan esta librería "spring-data-jpa"
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
//Habilitamos Feign en este microservicio para que pueda comunicarse con otros microservicios
@EnableFeignClients
//Habilitamos este microservicio como un cliente de un servidor Eureka para que pueda ser descubierto por este servidor y pueda ser registrado(Realmente poner esta anotación es opcional porque si no se pone, como este proyecto incluye la librería "spring-cloud-starter-netflix-eureka-client", automáticamente envía la señal a nuestro servidor Eureka para que se registre, pero está bien indicar esta anotación porque queda más claro y se entiende mejor)
@EnableEurekaClient
@SpringBootApplication
public class MicroservicioRespuestasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioRespuestasApplication.class, args);
	}

}
