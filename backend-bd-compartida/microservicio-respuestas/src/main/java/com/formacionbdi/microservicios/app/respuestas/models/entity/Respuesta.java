package com.formacionbdi.microservicios.app.respuestas.models.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Pregunta;

@Entity
@Table(name="respuestas")
public class Respuesta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String texto;
	
	// Relación one-to-many entre la clase entidad Alumno y esta clase entidad Respuesta(Un alumno puede estar asociado con muchas respuestas pero una respuesta sólo puede estar relacionada con un alumno)
	// Esta relación one-to-many es unidireccional porque no nos interesa tener una propiedad en la clase entidad Alumno relacionada con esta clase entidad Respuesta
	// Con el atributo "fetch = FetchType.LAZY", establecemos el tipo de carga Lazy, o carga perezosa, para obtener el alumno. De esta manera, el alumno se carga o se obtiene cuando se invoque al método "getAlumno" y no antes cuando se obtenga la respuesta como sí pasaría con el tipo de carga EAGER
	// Como la relación es one-to-Many, con la anotación @JoinColumn personalizamos la clave foránea que viaja desde la clase entidad Alumno a esta clase entidad con el nombre "alumno_id"
	// Este lado de la relación one-to-Many se corresponde con la parte ManyToOne, es decir, muchas respuestas pueden estar relacionadas con un alumno
	// Y la parte del lado de la clase entidad Alumno se corresponde con la parte OneToMany de esta relación one-to-many, es decir, un alumno puede estar relacionado con muchas respuestas
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "alumno_id")
	private Alumno alumno;
	
	// Relación one-to-one entre la clase entidad Pregunta y esta clase entidad Respuesta(Una pregunta sólo puede estar asociada con una respuesta y una respuesta sólo puede estar relacionada con una pregunta)
	// Esta relación one-to-many es unidireccional porque no nos interesa tener una propiedad en la clase entidad Pregunta relacionada con esta clase entidad Respuesta
	// Con el atributo "fetch = FetchType.LAZY", establecemos el tipo de carga Lazy, o carga perezosa, para obtener la pregunta. De esta manera, la pregunta se carga o se obtiene cuando se invoque al método "getPregunta" y no antes cuando se obtenga la respuesta como sí pasaría con el tipo de carga EAGER
	// Como la relación es one-to-one, con la anotación @JoinColumn personalizamos la clave foránea que viaja desde la clase entidad Pregunta a esta clase entidad con el nombre "pregunta_id"
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pregunta_id")
	private Pregunta pregunta;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
	
	
}
