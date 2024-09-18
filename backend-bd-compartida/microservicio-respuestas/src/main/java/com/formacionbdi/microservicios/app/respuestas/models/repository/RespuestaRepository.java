package com.formacionbdi.microservicios.app.respuestas.models.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;

public interface RespuestaRepository extends CrudRepository<Respuesta, Long> {

	// Consulta personalizada, usando la anotación @Query, que obtiene las respuestas de las preguntas de un exámen en concreto realizado por un determinado alumno. Para ello, le pasamos a la consulta el id del alumno y el id del exámen
	// Las consultas indicadas en las anotaciones @Query son consultas JPQL, es decir, las consultas se escriben teniendo en cuenta las clases entidad y no las tablas de la base de datos
	// Usamos los "joins" con "fetch" porque en este caso queremos devolver toda la información de cada una de las respuestas junto con sus atributos relacionados con otras entidades, es decir, queremos devolver toda la información de cada respuesta junto con toda la información de las preguntas del exámen y junto con toda la información de ese exámen
	@Query("select r from Respuesta r join fetch r.alumno a join fetch r.pregunta p join fetch p.examen e where a.id=?1 and e.id=?2")
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId);
	
	// Consulta personalizada, usando la anotación @Query, que obtiene los ids de los exámenes respondidos por un determinado alumno cuyo id se lo pasamos a la consulta
	// Las consultas indicadas en las anotaciones @Query son consultas JPQL, es decir, las consultas se escriben teniendo en cuenta las clases entidad y no las tablas de la base de datos
	// Usamos los "joins" sin "fetch" porque en este caso estamos devolviendo sólo los ids de los exámenes y no nos interesa devolver toda la información de los exámenes junto con sus atributos relacionados con otras entidades
	@Query("select e.id from Respuesta r join r.alumno a join r.pregunta p join p.examen e where a.id=?1 group by e.id")
	public Iterable<Long> findExamenesIdsConRespuestasByAlumno(Long alumnoId);
}
