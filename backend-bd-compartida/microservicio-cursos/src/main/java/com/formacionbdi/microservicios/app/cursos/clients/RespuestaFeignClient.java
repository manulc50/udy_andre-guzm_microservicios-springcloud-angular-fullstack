package com.formacionbdi.microservicios.app.cursos.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Un cliente Feign usa, por defecto y de manera automática, un balanceador de carga(Ribbon por defecto, que está actualmente en mantenimiento. Pero si lo deshabilitamos con el valor false en la propiedad "spring.cloud.loadbalancer.ribbon.enabled=false", se usará el balanceador de carga de Spring Cloud en su lugar) para realizar peticiones http a las distintas instancias, si las hay, del microservicio con el que va a comunicarse(En este caso, "microservicio-respuestas") 

// Al igual que ocurre con los repositorios que extienden de las interfaces de Spring Data Jpa como CrudRespository, PagingAndSortingRepository y JpaRepository, los métodos definidos en interfaces que son clientes Feign, como en este caso, son implementados automáticamente por Spring y sólo tenemos que definir el prototipo o esqueleto de esos métodos
// También, al igual que ocurre con los repositorios que extienden de las interfaces de Spring Data Jpa como CrudRespository, PagingAndSortingRepository y JpaRepository, cuando una interfaz tiene la anotación @FeignClient, Spring de forma automática guarda o almacena un bean con la implementación(la proporciona Spring) de esta interfaz para poder inyectarlo y usarlo en otra parte del proyecto

// Con esta anotación indicamos que esta interfaz se corresponde con un cliente Feign y va a comunicarse con nuestro microservicio llamado "microservicio-respuestas"
@FeignClient(name = "microservicio-respuestas")
public interface RespuestaFeignClient {

	// Realiza una petición http Get a la ruta "/alumno/{alumnoId}/examenes-respondidos" del microservicio "microservicio-respuestas"
	// Este método se puede llamar como nosotros queramos pero tiene que recibir los mismos parámetros de entrada que el método handler asociado a la ruta "/alumno/{alumnoId}/examenes-respondidos" del microservicio "microservicio-respuestas" y tiene que devolver la misma salida
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	@GetMapping("/alumno/{alumnoId}/examenes-respondidos")
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(@PathVariable Long alumnoId);
}
