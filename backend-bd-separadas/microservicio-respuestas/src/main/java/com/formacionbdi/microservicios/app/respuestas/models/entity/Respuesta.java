package com.formacionbdi.microservicios.app.respuestas.models.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Pregunta;

@Document(collection="respuestas")
public class Respuesta {
	
	@Id
	private String id;
	private String texto;
	
	// Cuando las tablas "alumnos" y "respuestas" compartían la misma base de datos, se usaba esta propiedad para establecer la relación one-to-many entre la clase entidad Alumno y esta clase entidad Respuesta mediante una clave Foreign Key, pero, ahora, como la tabla "alumnos" está en otra base de datos distinta, la relación se hace a través de la propiedad "alumnoId" de esta clase entidad y a través de la comunicación entre el microservicio "microservicio-alumnos" y este microservicio "microservicio-respuestas"
	// Como esta propiedad ya no establece la relación entre los alumnos y las respuestas a nivel de base de datos, ya no es necesario persistirla en la base de datos y, por eso, la anotamos con @Transient
	//@Transient // Con esta anotación indicamos que este atributo no es persistente en la base de datos y, por lo tanto, no se va a guardar en la base de datos
	private Alumno alumno;
	
	private Long alumnoId;

	// Cuando las tablas "preguntas" y "respuestas" compartían la misma base de datos, se usaba esta propiedad para establecer la relación one-to-one entre esta clase entidad Respuesta y la clase entidad Pregunta mediante una clave Foreign Key, pero, ahora, como la tabla "preguntas" está en otra base de datos distinta, la relación se hace a través de la propiedad "preguntaId" de esta clase entidad y a través de la comunicación entre el microservicio "microservicio-examenes" y este microservicio "microservicio-respuestas"
	// Como esta propiedad ya no establece la relación entre las preguntas y las respuestas a nivel de base de datos, ya no es necesario persistirla en la base de datos y, por eso, la anotamos con @Transient
	//@Transient // Con esta anotación indicamos que este atributo no es persistente en la base de datos y, por lo tanto, no se va a guardar en la base de datos
	private Pregunta pregunta;
	
	private Long preguntaId;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Pregunta getPregunta() {
		return pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public Long getPreguntaId() {
		return preguntaId;
	}

	public void setPreguntaId(Long preguntaId) {
		this.preguntaId = preguntaId;
	}
	
}
