package com.formacionbdi.microservicios.app.cursos.models.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;

public interface CursoRepository extends PagingAndSortingRepository<Curso,Long> {
	
	// Consulta personalizada, usando la anotación @Query, que obtiene el curso de un alumno a partir del id del alumno que le pasamos
	// Las consultas indicadas en las anotaciones @Query son consultas JPQL, es decir, las consultas se escriben teniendo en cuenta las clases entidad y no las tablas de la base de datos
	// Usamos los "joins" con "fetch" porque en este caso queremos devolver toda la información del curso junto con sus atributos relacionados con otras entidades, es decir, queremos devolver toda la información del curso junto con toda la información del alumno(la información del alumno se encuentra en la entidad CursoAlumno)
	@Query("select c from Curso c join fetch c.cursoAlumnos ca where ca.alumnoId=?1")
	public Curso findCursoByAlumnoId(Long id);
	
	// Consulta personalizada, usando la anotación @Query, que elimina una relación entre un alumno y un curso a partir del id de ese alumno que se le pasa a la consulta
	@Modifying // Esta anotación es para indicar que esta consulta no es de sólo lectura y se va a  modificar una tabla de la base de datos 
	@Query("delete from CursoAlumno ca where ca.alumnoId=?1")
	public void eliminarCursoAlumnoPorId(Long alumnoId);

}
