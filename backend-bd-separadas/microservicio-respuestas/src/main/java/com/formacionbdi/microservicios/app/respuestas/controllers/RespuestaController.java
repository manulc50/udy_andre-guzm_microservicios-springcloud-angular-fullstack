package com.formacionbdi.microservicios.app.respuestas.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;
import com.formacionbdi.microservicios.app.respuestas.services.RespuestaService;

@RestController
public class RespuestaController {
	
	@Autowired
	private RespuestaService respuestaService;
	
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Post con el parámetro de entrada "respuestas" de tipo Iterable<Respuesta>. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades de los objetos de tipo Respuesta contenidos en el iterable "respuestas" de tipo Iterable<Respuesta> que se está tratando
	@PostMapping // Si no especificamos una ruta, por defecto es la raíz, es decir, "\"
	public ResponseEntity<Iterable<Respuesta>> crear(@RequestBody Iterable<Respuesta> respuestas){
		respuestas = ((List<Respuesta>)respuestas).stream().map(r -> {
			r.setAlumnoId(r.getAlumno().getId());
			r.setPreguntaId(r.getPregunta().getId());
			return r;
		})
		.collect(Collectors.toList());
		
		Iterable<Respuesta> respuestasDb = respuestaService.saveAll(respuestas);
		return ResponseEntity.status(HttpStatus.CREATED).body(respuestasDb);
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	@GetMapping("/alumno/{alumnoId}/examen/{examenId}")
	public ResponseEntity<Iterable<Respuesta>> obtenerRespuestasPorAlumnoyExamen(@PathVariable Long alumnoId,@PathVariable Long examenId){
		Iterable<Respuesta> respuestasDb = respuestaService.findRespuestasByAlumnoByExamen(alumnoId, examenId);
		return ResponseEntity.ok().body(respuestasDb);
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	@GetMapping("/alumno/{alumnoId}/examenes-respondidos")
	public ResponseEntity<Iterable<Long>> obtenerExamenesIdsConRespuestasAlumno(@PathVariable Long alumnoId){
		Iterable<Long> examenesIds = respuestaService.findExamenesIdsConRespuestasByAlumno(alumnoId);
		return ResponseEntity.ok().body(examenesIds);
		
	}
}
