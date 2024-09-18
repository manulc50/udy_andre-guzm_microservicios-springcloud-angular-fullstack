package com.formacionbdi.microservicios.commons.examenes.models.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="asignaturas")
public class Asignatura {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	
	// Esta clase entidad Asignatura se relaciona consigo misma con un tipo de relación one-to-many(Una asignatura "padre" puede estar relacionada con muchas asignaturas "hijas" pero una asignatura "hija" sólo puede estar relacionada con una asignatura "padre", es decir, no puede tener más de una asignatura "padre")
	// Esta relación one-to-many es bidireccional porque tenemos propiedades de ambos lados de la relación, es decir, tenemos una propiedad que representa la asignatura "padre" y tenemos otra propiedad que representa las asignaturas "hijas"
	
	// Este lado de la relación one-to-Many se corresponde con la parte ManyToOne, es decir, muchas asignaturas "hijas" están relacionados con una asignatura "padre"
	// Con el atributo "fetch = FetchType.LAZY", establecemos el tipo de carga Lazy, o carga perezosa, para obtener la asignatura "padre". De esta manera, la asignatura "padre" se carga o se obtiene cuando se invoque al método "getPadre" y no antes cuando se obtenga la asignatura "hija" como sí pasaría con el tipo de carga EAGER
	// Con la anotación "@JoinColumn", personalizamos el nombre de la clave foránea con el nombre "asignatura_id"
	// Como la relación es bidireccional, para evitar un bucle o loop infinito entre asignaturas "hijas" y asginaturas "padres" a la hora de convertir o pasar sus datos a un Json para enviarlos en respuestas de peticiones http, tenemos que usar la anotación @JsonIgnoreProperties en este lado de la relación para que no renderice a un Json los datos de la propiedad "hijos" del otro lado de esta relación	
	// Como el tipo de carga para obtener los datos de esta propiedad es Lazy o perezosa, Hibernate utiliza un objeto Proxy que tiene, entre otras propiedades, las propiedades "handler" y "hibernateLazyInitializer" que a veces dan problemas a la hora de pasar los datos a un objeto Json para ser enviado en la respuesta de una petición http. Por esta razón, para evitar problemas con estas 2 propiedades, tenemos que ignorarlas a la hora de pasar los datos al objeto Json 
	@JsonIgnoreProperties(value= {"hijos","handler","hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="asignatura_id")
	private Asignatura padre;
	
	// Este lado de la relación one-to-Many se corresponde con la parte OneToMany, es decir, una asignatura "padre" esá relacionada con muchas asignaturas "hijas"
	// Con el atributo "fetch = FetchType.LAZY", establecemos el tipo de carga Lazy, o carga perezosa, para obtener las asignaturas "hijas". De esta manera, las asignatura "hijas" se cargan o se obtienen cuando se invoque al método "getHijas" y no antes cuando se obtenga la asignatura "padre" como sí pasaría con el tipo de carga EAGER
	// Con el atributo "mappedBy = "padre", establecemos que la relación one-to-many es bidireccional a partir de la propiedad "padre" del otro lado de la relación(Asignatura "padre")
	// Con "cascade = CascadeType.ALL" indicamos que todas las acciones que se hagan sobre asignaturas "padres" también se apliquen sobre sus correspondientes asignaturas "hijas". De esta manera, cuando se persista una asignatura "padre" en la base de datos, de manera automática también se persistirán sus asignaturas "hijas". Y lo mismo pasa cuando se elimine una asignatura "padre", es decir, también se eliminarán de forma automática sus asignaturas "hijas" 
	// Como la relación es bidireccional, para evitar un bucle o loop infinito entre asignaturas "padres" y asginaturas "hijas" a la hora de convertir o pasar sus datos a un Json para enviarlos en respuestas de peticiones http, tenemos que usar la anotación @JsonIgnoreProperties en este lado de la relación para que no renderice a un Json los datos de la propiedad "padre" del otro lado de esta relación
	// Como el tipo de carga para obtener los datos de esta propiedad es Lazy o perezosa, Hibernate utiliza un objeto Proxy que tiene, entre otras propiedades, las propiedades "handler" y "hibernateLazyInitializer" que a veces dan problemas a la hora de pasar los datos a un objeto Json para ser enviado en la respuesta de una petición http. Por esta razón, para evitar problemas con estas 2 propiedades, tenemos que ignorarlas a la hora de pasar los datos al objeto Json
	@JsonIgnoreProperties(value= {"padre","handler","hibernateLazyInitializer"}, allowSetters = true)
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "padre",cascade = CascadeType.ALL)
	private List<Asignatura> hijos;
	
	public Asignatura() {
		this.hijos = new ArrayList<Asignatura>();
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

	public Asignatura getPadre() {
		return padre;
	}

	public void setPadre(Asignatura padre) {
		this.padre = padre;
	}

	public List<Asignatura> getHijos() {
		return hijos;
	}

	public void setHijos(List<Asignatura> hijos) {
		this.hijos = hijos;
	}
	
}
