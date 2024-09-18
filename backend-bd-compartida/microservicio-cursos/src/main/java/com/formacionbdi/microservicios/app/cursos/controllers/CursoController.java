package com.formacionbdi.microservicios.app.cursos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.app.cursos.services.CursoService;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.controllers.CommonController;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;

@RestController
public class CursoController extends CommonController<Curso, CursoService>{

	@Value("${config.balanceador.test}")
	private String balanceadorTest;
	
	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("balanceador", balanceadorTest);
		response.put("cursos", service.findAll());
		return ResponseEntity.ok(response);
	}

	// Este método handler no lo podemos reutilizar usando nuestra librería "commons-microservicio" porque es especifico de la entidad Curso debido a que tenemos que especificar sus métodos getter y setter para la actualización de sus propiedades o campos
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "curso" de tipo Curso. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades del objeto "curso" de tipo Curso que se esté tratando
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, como ambos nombres son distintos, lo hacemos
	// El objeto "result" de tipo BindingResult siempre que hay que ponerlo a continuación de la entidad que se quiere validar con @Valid
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@PathVariable(name="id") Long idCurso,@Valid @RequestBody Curso curso, BindingResult result){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
	    Optional<Curso> oCurso = this.service.findById(idCurso);
	    if(!oCurso.isPresent()) {
	    	return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
	    }
	    Curso dbCurso = oCurso.get();
	    
	    dbCurso.setNombre(curso.getNombre());
	    // La propiedad "createAt" no se actualiza porque se trata de la fecha de creación de un nuevo curso
	    
	    return ResponseEntity.ok().body(this.service.save(dbCurso));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "alumnos" de tipo List<Alumno>. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades de los objetos de tipo Alumno de la lista "alumnos" que se esté tratando
	@PutMapping("/{id}/asignar-alumnos")
	public ResponseEntity<Curso> asignarAlumnos(@PathVariable Long id,@RequestBody List<Alumno> alumnos){
	    Optional<Curso> oCurso = this.service.findById(id);
	    
	    if(!oCurso.isPresent()) {
	    	return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
	    }
	    Curso dbCurso = oCurso.get();
	    
	    alumnos.forEach(a -> {
	    	dbCurso.addAlumno(a);
	    });
	    
	    return ResponseEntity.ok().body(this.service.save(dbCurso));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "alumno" de tipo Alumno. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades del objeto "alumno" de tipo Alumno que se esté tratando
	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<Curso> eliminarAlumno(@PathVariable Long id,@RequestBody Alumno alumno){
	    Optional<Curso> oCurso = this.service.findById(id);
	    
	    if(!oCurso.isPresent()) {
	    	return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
	    }
	    
	    Curso dbCurso = oCurso.get();
	    
	    dbCurso.removeAlumno(alumno);
	    
	    return ResponseEntity.ok().body(this.service.save(dbCurso));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, como ambos nombres son distintos, lo hacemos
	@GetMapping("/alumno/{id}")
	public ResponseEntity<Curso> buscarPorAlumnoId(@PathVariable(value="id") Long alumnoId){
		Curso curso = this.service.findCursoByAlumnoId(alumnoId);
		
		if(curso != null) {
			
			// Obtenemos una lista con los ids de los exámenes respondidos del alumno a través de la petición http Get que realiza este microservicio al microservicio "microservicio-respuestas" usando un cliente Feign
			List<Long> examenesIds = (List<Long>) this.service.obtenerExamenesIdsConRespuestasAlumno(alumnoId);
			
			List<Examen> examenes = curso.getExamenes().stream().map(examen -> {
				if(examenesIds.contains(examen.getId())) {
					examen.setRespondido(true);
				}
				return examen;
			}).collect(Collectors.toList());
			
			curso.setExamenes(examenes);
		}
		
		return ResponseEntity.ok(curso); // Otra opción es usar la expresión "ResponseEntity.ok().body(this.service.findCursoByAlumnoId(idAlumno))", es decir, la expresión "ResponseEntity.ok(this.service.findCursoByAlumnoId(idAlumno))" es un atajo de la expresión "ResponseEntity.ok().body(this.service.findCursoByAlumnoId(idAlumno))"
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "examenes" de tipo List<Examen>. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades de los objetos de tipo Examen de la lista "examenes" que se esté tratando
	@PutMapping("/{id}/asignar-examenes")
	public ResponseEntity<?> asignarExamenes(@PathVariable Long id,@RequestBody List<Examen> examenes){
	    Optional<Curso> oCurso = this.service.findById(id);
	    
	    if(!oCurso.isPresent()) {
	    	return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
	    }
	    
	    Curso dbCurso = oCurso.get();
	    
	    examenes.forEach(e -> {
	    	dbCurso.addExamen(e);
	    });
	    
	    return ResponseEntity.ok().body(this.service.save(dbCurso));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "examen" de tipo Examen. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades del objeto "examen" de tipo Examen que se esté tratando
	@PutMapping("/{id}/eliminar-examen")
	public ResponseEntity<?> eliminarExamen(@PathVariable Long id,@RequestBody Examen examen){
	    Optional<Curso> oCurso = this.service.findById(id);
	    
	    if(!oCurso.isPresent()) {
	    	return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
	    }
	    
	    Curso dbCurso = oCurso.get();
	    
	    dbCurso.removeExamen(examen);
	    
	    return ResponseEntity.ok().body(this.service.save(dbCurso));
	}
}
