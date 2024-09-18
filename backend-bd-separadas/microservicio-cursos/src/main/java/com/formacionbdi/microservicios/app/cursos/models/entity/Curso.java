package com.formacionbdi.microservicios.app.cursos.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;


@Entity
@Table(name="cursos")
public class Curso {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "no puede estar vacío")
	private String nombre;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP) // Con esta anotación podemos indicar si quqeremos guardar en el campo "create_at" de la tabla sólo la fecha(TemporalType.DATE), solo el tiempo(TemporalType.TIME) o ambas cosas(TemporalType.TIMESTAMP)
	private Date createAt;
	
	// Cuando las tablas "alumnos" y "cursos" compartían la misma base de datos, se usaba esta propiedad para establecer la relación one-to-many con la clase entidad Alumno mediante una clave Foreign Key, pero, ahora, como la tabla "alumnos" está en otra base de datos distinta, la relación se hace a través de la tabla intermedia "cursos_alumnos", mapeada con la calse entidad CursoAlumno, y a través de la comunicación entre el microservicio "microservicio-alumnos" y este microservicio "microservicio-cursos"
	// Como esta propiedad ya no establece la relación entre los alumnos y los cursos a nivel de base de datos, ya no es necesario persistirla en la base de datos y, por eso, la anotamos con @Transient
	@Transient // Con esta anotación indicamos que este atributo no es persistente en la base de datos y, por lo tanto, no se va a guardar en la base de datos
	private List<Alumno> alumnos;
	
	// Relación one-to-many entre esta clase entidad Curso y la clase entidad CursoAlumno(Esta clase entidad se mapea con una tabla intermedia de la base de datos para establecer la relación entre los alumnos y los cursos donde en un curso puede haber muchos alumnos pero un alumno sólo puede estar en un curso)
	// Esta relación es bidireccional porque hemos puesta la propiedad "curso" en la clase entidad CursoAlumno
	// Establecemos el tipo de carga Lazy, o carga perezosa, para obtener los objetos de la entidad CursoAlumno. De esta manera, estos objetos se cargan o se obtienen cuando se invoque al método "getCursoAlumnos" y no antes cuando se obtenga el curso como sí pasaría con el tipo de carga EAGER
	// Con "cascade = CascadeType.ALL" indicamos que todas las acciones que se hagan sobre objetos de esta entidad también se apliquen a objetos de la entidad relacionada CursoAlumno. De esta manera, cuando se persista un objeto de esta entidad en la base de datos, de manera automática también se persistirán los objetos de la entidad CursoAlumno relacionados con ese objeto de esta entidad. Y lo mismo pasa cuando se elimine un objeto de esta entidad, es decir, también se eliminarán de forma automática los objetos de la entidad CursoAlumno relacionados con ese objeto de esta entidad 
	// Esto lo hacemos así porque la relación entre esta entidad Curso y la entidad CursoAlumno es una relación fuerte, es decir, si desaparece un curso, los objeto de la entidad CursoAlumno relacionados con ese curso no pueden vivir por separado y también tienen que desaparecer
	// Con "orphanRemoval = true", cuando se elimine un objeto CursoAlumno de esta lista, o se ponga a null, de manera automática se eliminará de la base de datos inmediatamente
	// No confundir la opción "cascade = CascadeType.REMOVE" con la opción "orphanRemoval = true". La primera elimina, de forma automática, de la base de datos todos los objeto de tipo CursoAlumno relacionados con un curso cuando ese curso es eliminado de la base de datos. Y la segunda elimina, de forma automática, de la base de datos los objetos de tipo CursoAlumno relacionados con un curso que han sido desconectadas de ese curso(Por ejemplo, eliminando objetos de tipo CursoAlumno de esta lista "cursoAlumnos") pero ese curso sigue vivo, no se ha eliminado
	// Este lado de la relación one-to-Many se corresponde con la parte OneToMany, es decir, un curso está relacionado con muchas objetos de tipo CursoAlumno porque puede haber muchos alumnos en un curso
	// Y la parte del lado de la clase entidad CursoAlumno se corresponde con la parte ManyToOne de esta relación one-to-many, es decir, muchos objeto de tipo CursoAlumno pueden estar relacionados con un curso
	// Como la relación es bidireccional, tenemos que indicar el atributo "mappedby" con el nombre de la propiedad de la clase entidad CursoAlumno que hace referencia a esta entidad
	// Como la relación es bidireccional, para evitar un bucle o loop infinito entre objetos de esta entidad y objetos de la entidad CursoAlumno a la hora de convertir o pasar sus datos a un Json para enviarlos en respuestas de peticiones http, tenemos que usar la anotación @JsonIgnoreProperties pasándole el nombre de la propiedad("curso") de la clase entidad CursoAlumno que hace referencia a esta entidad para que sea ignorada a la hora de convertir los datos a formato Json
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "curso",cascade = CascadeType.ALL,orphanRemoval = true)
	@JsonIgnoreProperties(value = {"curso"}, allowSetters = true)
	private List<CursoAlumno> cursoAlumnos;
	
	// Relación many-to-many entre esta clase entidad Curso y la clase entidad Examen(Un curso puede estar asociado con muchos exámenes y un éxamen puede estar relacionado con muchos cursos)
	// Esta relación many-to-many es unidireccional porque no nos interesa tener una propiedad en la clase entidad Examen relacionada con esta clase entidad Curso
	// Con el atributo "fetch = FetchType.LAZY", establecemos el tipo de carga Lazy, o carga perezosa, para obtener los exámenes. De esta manera, los exámenes se cargan o se obtienen cuando se invoque al método "getExámenes" y no antes cuando se obtenga el curso como sí pasaría con el tipo de carga EAGER
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Examen> examenes;
	
	public Curso(){
		this.alumnos = new ArrayList<Alumno>();
		this.examenes = new ArrayList<Examen>();
		this.cursoAlumnos = new ArrayList<CursoAlumno>();
	}
	
	// Este método almacena la fecha actual del sistema en la propiedad "createAt" antes de persistirse o guardarse un objeto de esta clase entidad en su tabla correspondiente de la base de datos
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
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
	
	public List<CursoAlumno> getCursoAlumnos() {
		return cursoAlumnos;
	}

	public void setCursoAlumnos(List<CursoAlumno> cursoAlumnos) {
		this.cursoAlumnos = cursoAlumnos;
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
	
	// Método que añade una nueva relación entre un curso y un alumno
	public void addCursoAlumno(CursoAlumno cursoAlumno) {
		this.cursoAlumnos.add(cursoAlumno);
	}
	
	// Método que elimina una relación entre un curso y un alumno
	public void removeCursoAlumno(CursoAlumno cursoAlumno) {
		this.cursoAlumnos.remove(cursoAlumno);
	}
	
}
