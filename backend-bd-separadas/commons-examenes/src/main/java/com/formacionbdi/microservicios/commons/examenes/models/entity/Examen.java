package com.formacionbdi.microservicios.commons.examenes.models.entity;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="examenes")
public class Examen {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(min = 4,max = 30)
	@NotEmpty(message = "no puede estar vacío")
	private String nombre;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP) // Con esta anotación podemos indicar si quqeremos guardar en el campo "create_at" de la tabla sólo la fecha(TemporalType.DATE), solo el tiempo(TemporalType.TIME) o ambas cosas(TemporalType.TIMESTAMP)
	private Date createAt;
	
	// Relación one-to-many entre esta clase entidad Examen y la clase entidad Pregunta(No se permite que una pregunta esté en más de un exámen. Si se permitiese, la relación sería many-to-many)
	// Esta relación es bidireccional porque hemos puesta la propiedad "examen" en la clase entidad Pregunta
	// Establecemos el tipo de carga Lazy, o carga perezosa, para obtener las preguntas. De esta manera, las preguntas se cargan o se obtienen cuando se invoque al método "getPreguntas" y no antes cuando se obtenga el exámen como sí pasaría con el tipo de carga EAGER
	// Con "cascade = CascadeType.ALL" indicamos que todas las acciones que se hagan sobre objetos de esta entidad también se apliquen a objetos de la entidad relacionada Pregunta. De esta manera, cuando se persista un objeto de esta entidad en la base de datos, de manera automática también se persistirán los objetos de la entidad Pregunta relacionados con ese objeto de esta entidad. Y lo mismo pasa cuando se elimine un objeto de esta entidad, es decir, también se eliminarán de forma automática los objetos de la entidad Pregunta relacionados con ese objeto de esta entidad 
	// Esto lo hacemos así porque la relación entre esta entidad Examen y la entidad Pregunta es una relación fuerte, es decir, si desaparece un exámen, las preguntas relacionadas con ese exámen no pueden vivir por separado y también tienen que desaparecer
	// Con "orphanRemoval = true", cuando se elimine una pregunta de esta lista, o se ponga a null, de manera automática se eliminará de la base de datos inmediatamente
	// No confundir la opción "cascade = CascadeType.REMOVE" con la opción "orphanRemoval = true". La primera elimina, de forma automática, de la base de datos todas las preguntas de un exámen cuando ese exámen es eliminado de la base de datos. Y la segunda elimina, de forma automática, de la base de datos las preguntas de un exámen que han sido desconectadas de ese exámen(Por ejemplo, eliminando preguntas de la lista de prguntas de un exámen) pero ese exámen sigue vivo, no se ha eliminado
	// Este lado de la relación one-to-Many se corresponde con la parte OneToMany, es decir, un exámen está relacionado con muchas preguntas
	// Y la parte del lado de la clase entidad Pregunta se corresponde con la parte ManyToOne de esta relación one-to-many, es decir, muchas preguntas están relacionadas con un exámen
	// Como la relación es bidireccional, tenemos que indicar el atributo "mappedby" con el nombre de la propiedad de la clase entidad Pregunta que hace referencia a esta entidad
	// Como la relación es bidireccional, para evitar un bucle o loop infinito entre objetos de esta entidad y objetos de la entidad Pregunta a la hora de convertir o pasar sus datos a un Json para enviarlos en respuestas de peticiones http, tenemos que usar la anotación @JsonIgnoreProperties pasándole el nombre de la propiedad("examen") de la clase entidad Pregunta que hace referencia a esta entidad para que sea ignorada a la hora de convertir los datos a formato Json
	@JsonIgnoreProperties(value="examen", allowSetters = true)
	@OneToMany(mappedBy="examen",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Pregunta> preguntas;
	
	// Relación one-to-many entre la clase entidad Asignatura y esta clase entidad Examen(Una asignatura puede estar asociada con muchos exámenes pero un éxamen sólo puede estar relacionado con una asignatura)
	// Esta relación one-to-many es unidireccional porque no nos interesa tener una propiedad en la clase entidad Asignatura relacionada con esta clase entidad Examen
	// Con el atributo "fetch = FetchType.LAZY", establecemos el tipo de carga Lazy, o carga perezosa, para obtener la asignatura. De esta manera, la asignatura se carga o se obtiene cuando se invoque al método "getAsignatura" y no antes cuando se obtenga el exámen como sí pasaría con el tipo de carga EAGER
	// Como la relación es one-to-Many, con la anotación @JoinColumn personalizamos la clave foránea que viaja desde la clase entidad Asignatura a esta clase entidad con el nombre "asignatura_id"
	// Este lado de la relación one-to-Many se corresponde con la parte ManyToOne, es decir, muchos exámenes están relacionados con una asignatura
	// Y la parte del lado de la clase entidad Asignatura se corresponde con la parte OneToMany de esta relación one-to-many, es decir, una asignatura está relacionada con muchos exámenes
	// Como el tipo de carga para obtener los datos de esta propiedad es Lazy o perezosa, Hibernate utiliza un objeto Proxy que tiene, entre otras propiedades, las propiedades "handler" y "hibernateLazyInitializer" que a veces dan problemas a la hora de pasar los datos a un objeto Json para ser enviado en la respuesta de una petición http. Por esta razón, para evitar problemas con estas 2 propiedades, tenemos que ignorarlas a la hora de pasar los datos al objeto Json 
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name="asignatura_id")
	@JsonIgnoreProperties(value= {"handler","hibernateLazyInitializer"})
	@NotNull(message = "no puede estar vacío")
	private Asignatura asignatura;
	
	@Transient // Con esta anotación indicamos que este atributo no es persistente en la base de datos y, por lo tanto, no se va a guardar en la base de datos
	private boolean respondido; // Esta propiedad indica si el exámen ha sido respondido o no por el alumno
	
	public Examen() {
		this.preguntas = new ArrayList<Pregunta>();
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

	public List<Pregunta> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		// Vaciamos o limpiamos el contenido antiguo del ArraList "preguntas" para establecer el nuevo contenido de preguntas
		this.preguntas.clear();
		// Como la relación es bidireccional, tenemos que establecer también la relación en los objetos de la clase entidad Pregunta contenidos en la lista "preguntas" 
		preguntas.forEach(this::addPregunta); // "this::addPregunta" es una forma abreviada de poner "p -> this.addPregunta(p)". Esta abreviatura la podemos usar cuando todos los parámetros de entrada de una función lambda se pasan en la llamada a una función y es la única implementación de esa función lambda
	}
	
	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	public boolean isRespondido() {
		return respondido;
	}

	public void setRespondido(boolean respondido) {
		this.respondido = respondido;
	}

	// Método para añadir una pregunta al exámen
	public void addPregunta(Pregunta pregunta) {
		this.preguntas.add(pregunta);
		// Como la relación es bidireccional, también establecemos la relación en el objeto "pregunta" de la clase entidad Pregunta
		pregunta.setExamen(this);
	}
	
	// Método para eliminar una pregunta al exámen
	public void removePregunta(Pregunta pregunta) {
		this.preguntas.remove(pregunta);
		// Como la relación es bidireccional, también eliminamos la relación en el objeto "pregunta" de la clase entidad Pregunta
		// De esta manera, como el atributo "orphanRemoval" de la anotación @OneToMany es true y ya no hay relación entre este exámen y esta pregunta, se eliminará automáticamente esta pregunta de la base de datos 
		pregunta.setExamen(null);
	}
	
	// Sobrescribimos este método para poder implementar nuestra comparación de objetos Examen y así poder eliminar con éxito exámenes del ArrayList examenes de la clase entidad Curso
	// Es decir, cuando se invoca al método "remove" de un ArrayList, se invoca por debajo el método "equals" de los objetos contenidos en ese ArrayList para ver si alguno de ellos es igual al objeto que se le pasa al método "remove" para su eliminación
	@Override
	public boolean equals(Object obj) {
		
		// Si ambos objeto tienen la misma referencia, devolvemos true para indicar que son iguales
		if(this == obj)
			return true;
		
		// Si el objeto a comparar no es una instancia de Examen, devolvemos false porque no es un tipo de objeto válido para comparar
		if(!(obj instanceof Examen))
			return false;
		
		// En caso contrario, si el objeto a comparar sí es una instancia de Examen, devolvemos el resultado de comprobar sus ids
		Examen e = (Examen)obj;
		return this.id != null && this.id.equals(e.getId());
	}
	
}
