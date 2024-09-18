package com.formacionbdi.microservicios.app.cursos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.app.cursos.models.entity.CursoAlumno;
import com.formacionbdi.microservicios.app.cursos.services.CursoService;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.controllers.CommonController;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;

@RestController
public class CursoController extends CommonController<Curso,CursoService>{
	
	@Value("${config.balanceador.test}")
	private String balanceadorTest;
	
	@GetMapping("balanceador-test")
	public ResponseEntity<Map<String,Object>> balanceadorTest() {
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("balanceador",balanceadorTest);
		response.put("cursos",this.service.findAll());
		
		return ResponseEntity.ok(response);
	}
	
	@Override
	@GetMapping // Si no especificamos una ruta, por defecto es la raíz, es decir, "\"
	public ResponseEntity<Iterable<Curso>> listar() {
		List<Curso> cursos= ((List<Curso>)this.service.findAll()).stream()
				.map(curso -> {
					curso.getCursoAlumnos().forEach(ca -> {
						Alumno alumno = new Alumno();
						alumno.setId(ca.getAlumnoId());
						curso.addAlumno(alumno);
					});
					return curso;
				})
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(cursos);
				
	}
	
	@Override
	@GetMapping("/pagina")
	public ResponseEntity<Page<Curso>> listar(Pageable pageable){
		Page<Curso> cursos = service.findAll(pageable).map(curso ->{
			curso.getCursoAlumnos().forEach(ca -> {
				Alumno alumno = new Alumno();
				alumno.setId(ca.getAlumnoId());
				curso.addAlumno(alumno);
			});
			return curso;
		});
		return ResponseEntity.ok().body(cursos);
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<Curso> ver(@PathVariable Long id){
		Optional<Curso> oCurso = service.findById(id);
		
		if(oCurso.isEmpty())
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
		
		Curso curso = oCurso.get();
		if(!curso.getCursoAlumnos().isEmpty()) {
			List<Long> ids = curso.getCursoAlumnos().stream().map(ca -> ca.getAlumnoId()).collect(Collectors.toList());
			List<Alumno> alumnos = (List<Alumno>) service.obtenerAlumnosPorCurso(ids);
			curso.setAlumnos(alumnos);
		}
		
		return ResponseEntity.ok(curso); // Otra opción es usar la expresión "ResponseEntity.ok().body(curso)", es decir, la expresión "ResponseEntity.ok(curso)" es un atajo de la expresión "ResponseEntity.ok().body(curso)"
	}

	// Este método handler no lo podemos reutilizar usando nuestra librería "commons-microservicio" porque es especifico de la entidad Curso debido a que tenemos que especificar sus métodos getter y setter para la actualización de sus propiedades o campos
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "curso" de tipo Curso. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades del objeto "curso" de tipo Curso que se esté tratando
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, como ambos nombres son distintos, lo hacemos
	// El objeto "result" de tipo BindingResult siempre que hay que ponerlo a continuación de la entidad que se quiere validar con @Valid
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@PathVariable(name="id") Long idCurso,@Valid @RequestBody Curso curso, BindingResult result) {
		if(result.hasErrors())
			return this.validar(result);
		
		Optional<Curso> oCurso = this.service.findById(idCurso);
		
		if(!oCurso.isPresent())
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
	
		Curso cursoDb = oCurso.get();
		
		cursoDb.setNombre(curso.getNombre());
		// La propiedad "createAt" no se actualiza porque se trata de la fecha de creación de un nuevo curso
	
		return ResponseEntity.ok().body(this.service.save(cursoDb));
		
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "alumnos" de tipo List<Alumno>. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades de los objetos de tipo Alumno de la lista "alumnos" que se esté tratando
	@PutMapping("/{id}/asignar-alumnos")
	public ResponseEntity<Curso> asignarAlumnos(@PathVariable Long id,@RequestBody List<Alumno> alumnos){
		Optional<Curso> oCurso = this.service.findById(id);
		
		if(!oCurso.isPresent())
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
	
		Curso cursoDb = oCurso.get();
		
		alumnos.forEach(alumno -> {
			CursoAlumno cursoAlumno = new CursoAlumno();
			cursoAlumno.setAlumnoId(alumno.getId());
			cursoAlumno.setCurso(cursoDb);
			cursoDb.addCursoAlumno(cursoAlumno);
		});
		
		return ResponseEntity.ok().body(this.service.save(cursoDb));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "alumno" de tipo Alumno. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades del objeto "alumno" de tipo Alumno que se esté tratando
	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<Curso> eliminarAlumno(@PathVariable Long id,@RequestBody Alumno alumno){
		Optional<Curso> oCurso = this.service.findById(id);
		
		if(!oCurso.isPresent())
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
	
		Curso cursoDb = oCurso.get();
		
		CursoAlumno cursoAlumno = new CursoAlumno();
		cursoAlumno.setAlumnoId(alumno.getId());
		cursoDb.removeCursoAlumno(cursoAlumno);
		
		return ResponseEntity.ok().body(this.service.save(cursoDb));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "examenes" de tipo List<Examen>. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades de los objetos de tipo Examen de la lista "examenes" que se esté tratando
	@PutMapping("/{id}/asignar-examenes")
	public ResponseEntity<Curso> asignarExamenes(@PathVariable Long id,@RequestBody List<Examen> examenes){
		Optional<Curso> oCurso = this.service.findById(id);
		
		if(!oCurso.isPresent())
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
	
		Curso cursoDb = oCurso.get();
		
		examenes.forEach(examen -> cursoDb.addExamen(examen));
		
		return ResponseEntity.ok().body(this.service.save(cursoDb));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "examen" de tipo Examen. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades del objeto "examen" de tipo Examen que se esté tratando
	@PutMapping("/{id}/eliminar-examen")
	public ResponseEntity<Curso> eliminarExamen(@PathVariable Long id,@RequestBody Examen examen){
		Optional<Curso> oCurso = this.service.findById(id);
		
		if(!oCurso.isPresent())
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
	
		Curso cursoDb = oCurso.get();
		
		cursoDb.removeExamen(examen);
		
		return ResponseEntity.ok().body(this.service.save(cursoDb));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, como ambos nombres son distintos, lo hacemos
	@GetMapping("/alumno/{id}")
	public ResponseEntity<Curso> buscarPorAlumnoId(@PathVariable(value="id") Long alumnoId){
		Curso curso = this.service.findCursoByAlumnoId(alumnoId);
		
		if(curso != null) {
			// Obtenemos una lista con los ids de los exámenes respondidos del alumno a través de la petición http Get que realiza este microservicio al microservicio "microservicio-respuestas" usando un cliente Feign
			List<Long> examenesIds = (List<Long>)this.service.obtenerExamenesIdsConRespuestasAlumno(alumnoId);
			if(examenesIds != null && examenesIds.size() > 0) {
				List<Examen> examenes = curso.getExamenes().stream().map(examen -> {
					if(examenesIds.contains(examen.getId()))
						examen.setRespondido(true);
					
					return examen;
				})
				.collect(Collectors.toList());
				
				curso.setExamenes(examenes);
			}
		}
		
		return ResponseEntity.ok(curso); // Otra opción es usar la expresión "ResponseEntity.ok().body(this.service.findCursoByAlumnoId(idAlumno))", es decir, la expresión "ResponseEntity.ok(this.service.findCursoByAlumnoId(idAlumno))" es un atajo de la expresión "ResponseEntity.ok().body(this.service.findCursoByAlumnoId(idAlumno))"
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	@DeleteMapping("/eliminar-curso-alumno/{alumnoId}")
	public ResponseEntity<Void> eliminarCursoAlumnoPorId(@PathVariable Long alumnoId){
		this.service.eliminarCursoAlumnoPorId(alumnoId);
		return ResponseEntity.noContent().build(); // Devolvemos una respuesta vacía con estado de éxito 204(No Content)
	}

}
