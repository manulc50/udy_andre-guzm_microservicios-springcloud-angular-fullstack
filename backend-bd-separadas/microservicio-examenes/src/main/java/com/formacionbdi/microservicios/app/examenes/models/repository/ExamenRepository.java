package com.formacionbdi.microservicios.app.examenes.models.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;

public interface ExamenRepository extends PagingAndSortingRepository<Examen,Long> {
	
	// Consulta personalizada, usando la anotación @Query, que obtiene una lista de exámenes cuyos nombres contienen el término de búsqueda que le pasamos
	// Las consultas indicadas en las anotaciones @Query son consultas JPQL, es decir, las consultas se escriben teniendo en cuenta las clases entidad y no las tablas de la base de datos
	@Query("select e from Examen e where e.nombre like %?1%")
	public List<Examen> findByNombre(String termino);
	
	// Consulta personalizada, usando la anotación @Query, que obtiene los ids de los exámenes que contienen una serie de preguntas determinadas que son pasadas a la consulta
	// Las consultas indicadas en las anotaciones @Query son consultas JPQL, es decir, las consultas se escriben teniendo en cuenta las clases entidad y no las tablas de la base de datos
	// Usamos los "joins" sin "fetch" porque en este caso estamos devolviendo sólo los ids de los exámenes y no nos interesa devolver toda la información de los exámenes junto con sus atributos relacionados con otras entidades
	@Query("select e.id from Pregunta p join p.examen e where p.id in ?1 group by e.id")
	public Iterable<Long> findExamenesIdsByPreguntaIds(Iterable<Long> preguntaIds);
}
