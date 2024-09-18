package com.formacionbdi.microservicios.app.examenes.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.examenes.services.ExamenService;
import com.formacionbdi.microservicios.commons.controllers.CommonController;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Asignatura;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Pregunta;

@RestController
public class ExamenController extends CommonController<Examen, ExamenService>{

	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "examen" de tipo Examen. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades del objeto de tipo Examen que se esté tratando
	// El objeto "result" de tipo BindingResult siempre que hay que ponerlo a continuación de la entidad que se quiere validar con @Valid
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@PathVariable Long id,@Valid @RequestBody Examen examen, BindingResult result){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Examen> oExamen = this.service.findById(id);
		
		if(!oExamen.isPresent()) {
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
		}
		Examen examenDb = oExamen.get();
		
		// Actualizamos el nombre del exámen
		examenDb.setNombre(examen.getNombre());
		
		// Creamos una lista con las preguntas del exámen obtenido de la base de datos que van a ser eliminadas porque no están en la nueva lista de preguntas del exámen que se recibe como parámetro de entrada
		List<Pregunta> eliminadas = examenDb.getPreguntas()
		.stream()
		.filter(pDb -> !examen.getPreguntas().contains(pDb)) // El método "contains" invoca por debajo al método "equals" de la clase entidad Pregunta que hemos sobrescrito para implementar nuestra comparación de preguntas
		.collect(Collectors.toList());
		
		// Finalmente, eliminamos las preguntas del exámen de la base de datos, usando el método "removePregunta", a partir de la lista anterior 
		eliminadas.forEach(examenDb::removePregunta); // "examenDb::removePregunta" es una forma abreviada de poner "pDb -> examenDb.removePregunta(pDb)". Esta abreviatura la podemos usar cuando todos los parámetros de entrada de una función lambda se pasan en la llamada a una función y es la única implementación de esa función lambda
		
		// Actualizamos las preguntas del exámen de la base datos con las nuevas preguntas del nuevo exámen recibido como parámetro de entrada
		examenDb.setPreguntas(examen.getPreguntas());
		
		// Actualizamos la asignatura del exámen
		examenDb.setAsignatura(examen.getAsignatura());
		
		return ResponseEntity.ok().body(this.service.save(examenDb));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, como ambos nombres son distintos, lo hacemos
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<List<Examen>> filtrar(@PathVariable(name="term") String termino){
		return ResponseEntity.ok().body(this.service.findByNombre(termino));
	}
	
	@GetMapping("/asignaturas")
	public ResponseEntity<Iterable<Asignatura>> listarAsignaturas(){
		return ResponseEntity.ok().body(this.service.findAllAsignaturas());
	}
}
