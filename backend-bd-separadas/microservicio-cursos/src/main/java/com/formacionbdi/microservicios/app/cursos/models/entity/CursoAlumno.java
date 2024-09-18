package com.formacionbdi.microservicios.app.cursos.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="cursos_alumnos")
public class CursoAlumno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Con el atributo "name" de la anotación @Column, establecemos el nombre "alumno_id" para el campo de la tabla "cursos_alumnos" correspondiente a esta propiedad
	// Como este campo de la tabla "cursos_alumnos" va a guardar ids de alumnos, todos los valores de este campo tienen que ser únicos
	@Column(name = "alumno_id", unique = true)
	private Long alumnoId;
	
	// Relación one-to-many entre la clase entidad Curso y esta clase entidad CursoAlumno(Esta clase entidad se mapea con una tabla intermedia de la base de datos para establecer la relación entre los alumnos y los cursos donde en un curso puede haber muchos alumnos pero un alumno sólo puede estar en un curso)
	// Esta relación es bidireccional porque hemos puesta la propiedad "cursoAlumnos" en la clase entidad Curso
	// Con el atributo "fetch = FetchType.LAZY", establecemos el tipo de carga Lazy, o carga perezosa, para obtener el curso. De esta manera, el curso se carga o se obtiene cuando se invoque al método "getCurso" y no antes cuando se obtenga el objeto de tipo CursoAlumno como sí pasaría con el tipo de carga EAGER
	// Como la relación es one-to-Many, con la anotación @JoinColumn personalizamos la clave foránea que viaja desde la clase entidad Curso a esta clase entidad con el nombre "curso_id"
	// Este lado de la relación one-to-Many se corresponde con la parte ManyToOne, es decir, muchos objetos de esta clase entidad CursoAlumno pueden estar relacionados con un curso, o dicho de otra manera, muchos alumnos pueden estar relacionados con un curso
	// Y la parte del lado de la clase entidad Curso se corresponde con la parte OneToMany de esta relación one-to-many, es decir, un curso puede estar relacionado con muchos objetos de esta clase entidad CursoAlumno, o dicho de otra manera, un curso puede estar relacionado con muchos alumnos
	// Como la relación es bidireccional, para evitar un bucle o loop infinito entre objetos de esta entidad y objetos de la entidad Curso a la hora de convertir o pasar sus datos a un Json para enviarlos en respuestas de peticiones http, tenemos que usar la anotación @JsonIgnoreProperties pasándole el nombre de la propiedad("cursoAlumnos") de la clase entidad Curso que hace referencia a esta entidad para que sea ignorada a la hora de convertir los datos a formato Json
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id")
	@JsonIgnoreProperties(value = {"cursoAlumnos"})
	private Curso curso;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	// Sobrescribimos este método para poder implementar nuestra comparación de objetos CursoAlumno y así poder eliminar con éxito relaciones entre cursos y alumnos del ArrayList de cursoAlumnos de la clase entidad Curso
	// Es decir, cuando se invoca al método "remove" de un ArrayList, se invoca por debajo el método "equals" de los objetos contenidos en ese ArrayList para ver si alguno de ellos es igual al objeto que se le pasa al método "remove" para su eliminación
	@Override
	public boolean equals(Object obj) {
		
		// Si ambos objeto tienen la misma referencia, devolvemos true para indicar que son iguales
		if(this == obj)
			return true;
		
		// Si el objeto a comparar no es una instancia de CursoAlumno, devolvemos false porque no es un tipo de objeto válido para comparar
		if(!(obj instanceof CursoAlumno))
			return false;
		
		// En caso contrario, si el objeto a comparar sí es una instancia de CursoAlumno, devolvemos el resultado de comprobar los ids de los alumnos
		CursoAlumno ca = (CursoAlumno)obj;
		return this.alumnoId != null && this.alumnoId.equals(ca.getAlumnoId());
	}

}
