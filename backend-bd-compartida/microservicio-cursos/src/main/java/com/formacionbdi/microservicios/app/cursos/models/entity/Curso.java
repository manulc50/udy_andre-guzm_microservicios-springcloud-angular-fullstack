package com.formacionbdi.microservicios.app.cursos.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;

@Entity
@Table(name="cursos")
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String nombre;
	
	@Column(name="create_at")
	@Temporal(TemporalType.TIMESTAMP) // Con esta anotación podemos indicar si quqeremos guardar en el campo "create_at" de la tabla sólo la fecha(TemporalType.DATE), solo el tiempo(TemporalType.TIME) o ambas cosas(TemporalType.TIMESTAMP)
	private Date createAt;
	
	// Relación one-to-many entre esta clase entidad Curso y la clase entidad Alumno(En un curso puede haber muchos alumnos pero un alumno sólo puede estar en un curso)
	// Esta relación es unidireccional porque no no nos interesa tener una propiedad en la clase entidad Alumno relacionada con esta clase entidad Curso
	// Establecemos el tipo de carga Lazy, o carga perezosa, para obtener los alumnos. De esta manera, los alumnos se cargan o se obtienen cuando se invoque al método "getAlumnos" y no antes cuando se obtenga el curso como sí pasaría con el tipo de carga EAGER
	// Como la relación es one-to-Many unidireccional, el lugar de crear una tabla intermedia que relacione alumnos y cursos, con la anotación @JoinColumn creamos una clave foránea que viajará desde esta clase entidad Curso a la clase entidad Alumno y la personalizamos con el nombre "curso_id"
	// Respecto a optimizar el número de consultas que realiza Hibernate, la anotación @JoinColumn sólo es óptima usarla junto con una anotación @ManyToOne en el lado ManyToOne de una relación one-to-many unidireccional o junto con una anotación @ManyToOne en el lado ManyToOne de una relación one-to-many bidireccional 
	// Este lado de la relación one-to-Many se corresponde con la parte OneToMany, es decir, un curso puede estar relacionado con muchos alumnos
	// Y la parte del lado de la clase entidad Alumno se corresponde con la parte ManyToOne de esta relación one-to-many, es decir, muchos alumnos pueden estar relacionados con un curso
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id") // Poner esta anotación aquí no optimiza el número de consultas que realiza Hibernate porque no estamos en el lado ManyToOne de la relación one-to-many, pero, como no estamos estableciendo una relación one-to-many bidireccional, si no usamos esta anotación, se creará una tabla intermedia para los alumnos y cursos, que tampoco es óptimo. Así que la solución más óptima en este caso sería crear una relación bidireccional para esta relación one-to-many
	private List<Alumno> alumnos;
	
	// Relación many-to-many entre esta clase entidad Curso y la clase entidad Examen(Un curso puede estar asociado con muchos exámenes y un éxamen puede estar relacionado con muchos cursos)
	// Esta relación many-to-many es unidireccional porque no nos interesa tener una propiedad en la clase entidad Examen relacionada con esta clase entidad Curso
	// Con el atributo "fetch = FetchType.LAZY", establecemos el tipo de carga Lazy, o carga perezosa, para obtener los exámenes. De esta manera, los exámenes se cargan o se obtienen cuando se invoque al método "getExámenes" y no antes cuando se obtenga el curso como sí pasaría con el tipo de carga EAGER
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Examen> examenes;

	// Este método almacena la fecha actual del sistema en la propiedad "createAt" antes de persistirse o guardarse un objeto de esta clase entidad en su tabla correspondiente de la base de datos
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}

	public Curso() {
		this.alumnos = new ArrayList<>();
		this.examenes = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}
	
	public List<Examen> getExamenes() {
		return examenes;
	}

	public void setExamenes(List<Examen> examenes) {
		this.examenes = examenes;
	}
	
	// Método que añade un nuevo alumno al curso
	public void addAlumno(Alumno alumno) {
		this.alumnos.add(alumno);
	}

	// Método que elimina un alumno del curso
	public void removeAlumno(Alumno alumno) {
		this.alumnos.remove(alumno);
	}

	// Método que añade un nuevo examen al curso
	public void addExamen(Examen examen) {
		this.examenes.add(examen);
	}
	
	// Método que elimina un examen del curso
	public void removeExamen(Examen examen) {
		this.examenes.remove(examen);
	}
}
