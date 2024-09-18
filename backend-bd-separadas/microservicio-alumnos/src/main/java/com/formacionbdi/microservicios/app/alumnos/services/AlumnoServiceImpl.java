package com.formacionbdi.microservicios.app.alumnos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.alumnos.clients.CursoFeignClient;
import com.formacionbdi.microservicios.app.alumnos.models.repository.AlumnoRepository;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.services.CommonServiceImpl;

@Service
public class AlumnoServiceImpl extends CommonServiceImpl<Alumno,AlumnoRepository> implements AlumnoService{

	@Autowired
	private CursoFeignClient cursoClient;
	
	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public List<Alumno> findByNombreOrApellidos(String termino) {
		return this.repository.findByNombreOrApellidos(termino);
	}

	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public Iterable<Alumno> findAllById(Iterable<Long> ids) {
		return this.repository.findAllById(ids);
	}

	@Override
	public void eliminarCursoAlumnoPorId(Long alumnoId) {
		cursoClient.eliminarCursoAlumnoPorId(alumnoId);
		
	}

	@Transactional // No ponemos "readOnly = true" porque se va a modificar una tabla de la base de datos
	@Override
	public void deleteById(Long id) {
		super.deleteById(id);
		this.eliminarCursoAlumnoPorId(id);
	}

	// Como estamos usando una base de datos Postgres para este microservicio, cuando actualizamos un registro alumno en la tabla "alumnos", Postgres pone ese registro que se acaba de actualizar al final de la tabla como si se hubiera creado un nuevo registro y, cuando recuperamos todos los alumnos de esa tabla, los devuelve de manera desordenada y no queremos ésto
	// Por lo tanto, para solucionar este asunto, sobrescribimos este método de la clase "CommonServiceImpl" para invocar a nuestro método "findAllByOrderByIdAsc" del repositorio "AlumnoRepository" con nuestra consulta personalizada para obtener todos los alumnos ordenados por su id de manera ascendente en lugar de invocar la método "findAll" que nos da directamente Spring Data JPA
	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public Iterable<Alumno> findAll() {
		return this.repository.findAllByOrderByIdAsc();
	}

	// Hacemos lo mismo que en el método anterior pero aplicado al caso de alumnos con paginación
	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public Page<Alumno> findAll(Pageable pageable) {
		return this.repository.findAllByOrderByIdAsc(pageable);
	}
	
	
	

}
