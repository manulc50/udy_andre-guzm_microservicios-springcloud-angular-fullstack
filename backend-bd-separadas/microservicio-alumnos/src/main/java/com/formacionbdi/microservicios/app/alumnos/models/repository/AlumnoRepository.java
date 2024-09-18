package com.formacionbdi.microservicios.app.alumnos.models.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;

public interface AlumnoRepository extends PagingAndSortingRepository<Alumno,Long>{

	// Consulta personalizada, usando la anotación @Query, que obtiene una lista de alumnos cuyos nombres o apellidos contienen el término de búsqueda que le pasamos
	// Las consultas indicadas en las anotaciones @Query son consultas JPQL, es decir, las consultas se escriben teniendo en cuenta las clases entidad y no las tablas de la base de datos
	// Como la base de datos que usa este microservicio "microservicio-alumnos" es una base de datos Postgres y es sensible a mayúsculas y a minúsculas, vamos a usar la función "upper" en esta consulta para que el filtrado de nombres o apellidos de alumnos se haga correctamente tanto si esos nombres o apellidos tienen letras en mayúscula como en minúscula
	// La función "upper" pone todas las letras de una cadena de texto en mayúscula. De esta manera, aunque en los nombres o apellidos de los alumnos haya letras en minúscula, el filtrado va a funcionar bien porque todas las comparaciones se hacen con todas las letras en mayúscula
	@Query("select a from Alumno a where upper(a.nombre) like upper(concat('%',?1,'%')) or upper(a.apellidos) like upper(concat('%',?1,'%'))")
	public List<Alumno> findByNombreOrApellidos(String termino);
	
	// Como estamos usando una base de datos Postgres para este microservicio, cuando actualizamos un registro alumno en la tabla "alumnos", Postgres pone ese registro que se acaba de actualizar al final de la tabla como si se hubiera creado un nuevo registro y, cuando recuperamos todos los alumnos de esa tabla, los devuelve de manera desordenada y no queremos ésto
	// Por lo tanto, para solucionar este asunto, creamos esta consulta personalizada para que devuelva todos los registros de alumnos de la tabla "alumnos" ordenador por el id de manera ascendente
	// En este caso, como estamos respetando la nomenclatura indicada por Spring Data JPA, no usamos la anotación @Query para implementar la consulta JPQL y, en su lugar, dejamos que lo haga Spring Data JPA automáticamente
	public Iterable<Alumno> findAllByOrderByIdAsc();
	
	// Hacemos lo mismo que en la consulta personalizada anterior, pero aplicado al caso de registros de alumnos con paginación
	public Page<Alumno> findAllByOrderByIdAsc(Pageable pageable);

}
