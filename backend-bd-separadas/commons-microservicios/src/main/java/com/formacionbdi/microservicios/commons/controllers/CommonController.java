package com.formacionbdi.microservicios.commons.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.formacionbdi.microservicios.commons.services.CommonService;


// Implementación del controlador genérico para todos los microservicios
// "E" representa una entidad genérica de nuestro modelo(Alumno, Curso, Exámen, etc...)

// Con esta anotación configuramos el CORS para que nuestros clientes puedan realizar peticiones http a este backend API REST
// Sólo se permite al cliente que se ejecuta en el puerto 4200 de localhost(nuestro cliente Angular) poder realizar peticiones http a este backend API REST
// Comentamos esta anotación porque ahora tenemos configurado el CORS en nuestro microservicio Gateway para que dicha configuración esté centralizada para todos nuestros microservicios que tienen controladores API REST
// @CrossOrigin({"http://localhost:4200"}) // Con "*" permitimos o habilitamos a cualquier cliente poder realizar peticiones http a este backend API REST
public class CommonController<E,S extends CommonService<E>> {

	@Autowired
	protected S service; // Usamos la restricción "protected" para que esta propiedad pueda ser usada por las clases hijas que hereden esta clase padre CommonController<E>
	
	@GetMapping // Si no especificamos una ruta, por defecto es la raíz, es decir, "\"
	public ResponseEntity<Iterable<E>> listar(){
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@GetMapping("/pagina")
	public ResponseEntity<Page<E>> listar(Pageable pageable){
		return ResponseEntity.ok().body(service.findAll(pageable));
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	@GetMapping("/{id}")
	public ResponseEntity<E> ver(@PathVariable Long id){
		Optional<E> oEntidad = service.findById(id);
		
		if(oEntidad.isEmpty())
			return ResponseEntity.notFound().build(); // Devolvemos una respuesta vacía con estado de error 404(Not Found)
		
		return ResponseEntity.ok(oEntidad.get()); // Otra opción es usar la expresión "ResponseEntity.ok().body(oEntidad.get())", es decir, la expresión "ResponseEntity.ok(oEntidad.get())" es un atajo de la expresión "ResponseEntity.ok().body(oEntidad.get())"
	}
	
	// El objeto "result" de tipo BindingResult siempre que hay que ponerlo a continuación de la entidad que se quiere validar con @Valid
	// La anotación @RequestBody mapea los datos que nos llegan en el cuerpo o body de la petición http Post con el parámetro de entrada "entidad" de tipo genérico E. Evidentemente, esos datos que nos llegan tienen que coincidir con las propiedades del objeto "entidad" de tipo genérico E que se esté tratando
	@PostMapping // Si no especificamos una ruta, por defecto es la raíz, es decir, "\"
	public ResponseEntity<?> crear(@Valid @RequestBody E entidad, BindingResult result) {
		if(result.hasErrors())
			return this.validar(result);
		
		E entidadDb = service.save(entidad);
		return ResponseEntity.status(HttpStatus.CREATED).body(entidadDb);
	}
	
	// Si el nombre de la variable que viaja con la url es distinto del nombre del parámetro de entrada que la recibe, es necesario usar el atributo "name" o "value"(alias del atributo "name") en la anotación @PathVariable, indicando el nombre de la variable que viaja en la url, para realizar el mapeo con ese parámetro de entrada. En este caso, ambos nombres son iguales y no hace falta hacerlo
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id){
		service.deleteById(id);
		return ResponseEntity.noContent().build(); // Devolvemos una respuesta vacía con estado de éxito 204(No Content)
	}
	
	protected ResponseEntity<Map<String,Object>> validar(BindingResult result){
		Map<String,Object> errores = new HashMap<String,Object>();
		result.getFieldErrors().forEach(err -> errores.put(err.getField(),"El campo " + err.getField() + " " + err.getDefaultMessage()));
		
		return ResponseEntity.badRequest().body(errores); // 400(Bad Request)
	}
	
}
