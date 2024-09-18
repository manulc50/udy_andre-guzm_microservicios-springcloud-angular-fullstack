package com.formacionbdi.microservicios.commons.examenes.models.entity;

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
@Table(name="preguntas")
public class Pregunta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String texto;
	
	// Relación one-to-many entre la clase entidad Examen y esta clase entidad Pregunta(No se permite que una pregunta esté en más de un exámen. Si se permitiese, la relación sería many-to-many)
	// Esta relación es bidireccional porque hemos puesta la propiedad "pregunta" en la clase entidad Examen
	// Con el atributo "fetch = FetchType.LAZY", establecemos el tipo de carga Lazy, o carga perezosa, para obtener el exámen. De esta manera, el exámen se carga o se obtiene cuando se invoque al método "getExamen" y no antes cuando se obtenga la pregunta como sí pasaría con el tipo de carga EAGER
	// Como la relación es one-to-Many, con la anotación @JoinColumn personalizamos la clave foránea que viaja desde la clase entidad Examen a esta clase entidad con el nombre "examen_id"
	// Este lado de la relación one-to-Many se corresponde con la parte ManyToOne, es decir, muchas preguntas están relacionadas con un exámen
	// Y la parte del lado de la clase entidad Examen se corresponde con la parte OneToMany de esta relación one-to-many, es decir, un exámen está relacionado con muchas preguntas
	// Como la relación es bidireccional, para evitar un bucle o loop infinito entre objetos de esta entidad y objetos de la entidad Examen a la hora de convertir o pasar sus datos a un Json para enviarlos en respuestas de peticiones http, tenemos que usar la anotación @JsonIgnoreProperties pasándole el nombre de la propiedad("preguntas") de la clase entidad Examen que hace referencia a esta entidad para que sea ignorada a la hora de convertir los datos a formato Json
	@JsonIgnoreProperties(value="preguntas")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="examen_id")
	private Examen examen;
	
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

	public Examen getExamen() {
		return examen;
	}

	public void setExamen(Examen examen) {
		this.examen = examen;
	}
	
	// Sobrescribimos este método para poder implementar nuestra comparación de objetos Pregunta y así poder eliminar con éxito alumnos del ArrayList preguntas de la clase entidad Examen
	// Es decir, cuando se invoca al método "remove" de un ArrayList, se invoca por debajo el método "equals" de los objetos contenidos en ese ArrayList para ver si alguno de ellos es igual al objeto que se le pasa al método "remove" para su eliminación
	@Override
	public boolean equals(Object obj) {
		
		// Si ambos objeto tienen la misma referencia, devolvemos true para indicar que son iguales
		if(this == obj)
			return true;
		
		// Si el objeto a comparar no es una instancia de Pregunta, devolvemos false porque no es un tipo de objeto válido para comparar
		if(!(obj instanceof Pregunta))
			return false;
		
		// En caso contrario, si el objeto a comparar sí es una instancia de Pregunta, devolvemos el resultado de comprobar sus ids
		Pregunta p = (Pregunta)obj;
		return this.id != null && this.id.equals(p.getId());
	}
}
