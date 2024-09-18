package com.formacionbdi.microservicios.app.respuestas.models.repositoty;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;

public interface RespuestaRepository extends MongoRepository<Respuesta,String> {
	
	// Consulta personalizada, usando la anotación @Query, que obtiene las respuestas de una serie de preguntas respondidas por un determinado alumno. Para ello, le pasamos a la consulta el id del alumno y los ids de las preguntas
	// La consulta indicada en la anotación @Query es una consulta de MongoDB, es decir, la consulta se escribe usando objetos Json. Los parámetros que se le pasan a una consulta en MongoDB empiezan en 0(?0), sin embargo, en una consulta JPQL los parámetros que se le pasan a una consulta empiezan en 1(?1)
	@Query("{ 'alumnoId': ?0,'preguntaId': { $in: ?1 } }")
	public Iterable<Respuesta> findRespuestaByAlumnoByPreguntaIds(Long alumnoId, Iterable<Long> preguntaIds);
	
	// Consulta personalizada, usando la anotación @Query, que obtiene las respuestas respondidas por un determinado alumno. Para ello, le pasamos a la consulta el id del alumno
	// La consulta indicada en la anotación @Query es una consulta de MongoDB, es decir, la consulta se escribe usando objetos Json. Los parámetros que se le pasan a una consulta en MongoDB empiezan en 0(?0), sin embargo, en una consulta JPQL los parámetros que se le pasan a una consulta empiezan en 1(?1)
	@Query("{ 'alumnoId': ?0 }")
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId);
	
	// Consulta personalizada, usando la anotación @Query, que obtiene las respuestas de las preguntas de un exámen en concreto realizado por un determinado alumno. Para ello, le pasamos a la consulta el id del alumno y el id del exámen
	// La consulta indicada en la anotación @Query es una consulta de MongoDB, es decir, la consulta se escribe usando objetos Json. Los parámetros que se le pasan a una consulta en MongoDB empiezan en 0(?0), sin embargo, en una consulta JPQL los parámetros que se le pasan a una consulta empiezan en 1(?1)
	@Query("{ 'alumnoId': ?0, 'pregunta.examen.id': ?1 }")
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId);

	// Consulta personalizada, usando la anotación @Query, que obtiene sólo los ids de los exámenes respondidos por un determinado alumno y los devuelve encapsulados en objetos de tipo Respuesta. Para ello, le pasamos a la consulta el id del alumno
	// La consulta indicada en la anotación @Query es una consulta de MongoDB, es decir, la consulta se escribe usando objetos Json. Los parámetros que se le pasan a una consulta en MongoDB empiezan en 0(?0), sin embargo, en una consulta JPQL los parámetros que se le pasan a una consulta empiezan en 1(?1)
	@Query(value = "{ 'alumnoId': ?0 }", fields = "{ 'pregunta.examen.id': 1 }")
	public Iterable<Respuesta> findExamenesIdsConRespuestasByAlumno(Long alumnoId);

}
