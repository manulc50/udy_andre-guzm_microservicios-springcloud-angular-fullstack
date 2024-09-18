package com.formacionbdi.microservicios.app.alumnos.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.formacionbdi.microservicios.app.alumnos.services.AlumnoService;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.controllers.CommonController;

@RestController
public class AlumnoController extends CommonController<Alumno, AlumnoService>{
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<Resource> verFoto(@PathVariable Long id){
	
		Optional<Alumno> oAlumno = this.service.findById(id);
		
		if(oAlumno.isEmpty() || oAlumno.get().getFoto() == null) {
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
		}
		
		// Creamos un recurso(Resource es una interfaz implementada en este caso por la clase ByteArrayResource)  de tipo ByteArrayResource, a partir del array de bytes de la foto del alumno, para devolverlo en la respuesta de la petición http
		Resource imagen = new ByteArrayResource(oAlumno.get().getFoto());
		
		// Creamos la respuesta de la petición http con estado OK(200) y con el recurso creado en el paso anterior como cuerpo de la respuesta en formato JPEG
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(imagen);
	}
	
	// Este método handler no lo podemos reutilizar usando nuestra librería "commons-microservicio" porque es especifico de la entidad Alumno debido a que tenemos que especificar sus métodos getter y setter para la actualización de sus propiedades o campos
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Put con el parámetro de entrada "alumno" de tipo Alumno. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades del objeto "alumno" de tipo Alumno que se esté tratando
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, como ambos nombres son distintos, lo hacemos
	// El objeto "result" de tipo BindingResult siempre que hay que ponerlo a continuación de la entidad que se quiere validar con @Valid
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@PathVariable(name="id") Long idAlumno,@Valid @RequestBody Alumno alumno, BindingResult result){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Alumno> oAlumno = this.service.findById(idAlumno);
		
		if(oAlumno.isEmpty()) {
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
		}
		
		Alumno alumnoDb = oAlumno.get();
		alumnoDb.setNombre(alumno.getNombre());
		alumnoDb.setApellidos(alumno.getApellidos());
		alumnoDb.setEmail(alumno.getEmail());
		// La propiedad "createAt" no se actualiza porque se trata de la fecha de creación de un nuevo alumno
		
		return ResponseEntity.ok().body(this.service.save(alumnoDb));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, como ambos nombres son distintos, lo hacemos
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<List<Alumno>> filtrar(@PathVariable(value="term") String termino){
		return ResponseEntity.ok().body(this.service.findByNombreOrApellido(termino));
	}

	// Los archivo multimedia como fotos, vídeos, documentos, etc... viajan en las peticiones http en campos de tipo Multipart de formularios FormData y no viajan en objetos Json
	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> crearConFoto(@Valid Alumno alumno, BindingResult result, @RequestParam MultipartFile archivo) throws IOException {
		if(!archivo.isEmpty()) {
			alumno.setFoto(archivo.getBytes());
		}
		return super.crear(alumno, result);
	}
	
	// Este método handler no lo podemos reutilizar usando nuestra librería "commons-microservicio" porque es especifico de la entidad Alumno debido a que tenemos que especificar sus métodos getter y setter para la actualización de sus propiedades o campos
	// Los archivo multimedia como fotos, vídeos, documentos, etc... viajan en las peticiones http en campos de tipo Multipart de formularios FormData y no viajan en objetos Json
	// En esta caso, como también editamos un alumno para asignarle una foto y ese archivo viaja a través de un Multipart FormData, el resto de los datos o propiedades del alumno también viajan en el FormData y no en un objeto Json. Por esta razón, no ponemos la anotación @RequestBody en la entidad "alumno" de tipo Alumno
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, como ambos nombres son distintos, lo hacemos
	// El objeto "result" de tipo BindingResult siempre que hay que ponerlo a continuación de la entidad que se quiere validar con @Valid
	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> editarConFoto(@PathVariable(name="id") Long idAlumno,@Valid Alumno alumno, BindingResult result,@RequestParam MultipartFile archivo) throws IOException{
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Alumno> oAlumno = this.service.findById(idAlumno);
		
		if(oAlumno.isEmpty()) {
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
		}
		
		Alumno alumnoDb = oAlumno.get();
		alumnoDb.setNombre(alumno.getNombre());
		alumnoDb.setApellidos(alumno.getApellidos());
		alumnoDb.setEmail(alumno.getEmail());
		// La propiedad "createAt" no se actualiza porque se trata de la fecha de creación de un nuevo alumno
		
		if(!archivo.isEmpty()) {
			alumnoDb.setFoto(archivo.getBytes());
		}
		
		return ResponseEntity.ok().body(this.service.save(alumnoDb));
	}

}
