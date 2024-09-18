package com.formacionbdi.microservicios.app.alumnos.models.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

public interface AlumnoRepository extends PagingAndSortingRepository<Alumno, Long> {

	// Consulta personalizada, usando la anotación @Query, que obtiene una lista de alumnos cuyos nombres o apellidos contienen el término de búsqueda que le pasamos
	// Las consultas indicadas en las anotaciones @Query son consultas JPQL, es decir, las consultas se escriben teniendo en cuenta las clases entidad y no las tablas de la base de datos
	@Query("select a from Alumno a where a.nombre like %?1% or a.apellido like %?1%")
	public List<Alumno> findByNombreOrApellido(String term);
}
