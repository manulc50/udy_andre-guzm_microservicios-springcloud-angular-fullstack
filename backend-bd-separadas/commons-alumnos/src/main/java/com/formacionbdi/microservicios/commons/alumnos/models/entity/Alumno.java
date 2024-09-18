package com.formacionbdi.microservicios.commons.alumnos.models.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="alumnos")
public class Alumno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "no puede estar vacío")
	private String nombre;
	
	@NotEmpty(message = "no puede estar vacío")
	private String apellidos;
	
	@NotEmpty(message = "no puede estar vacío")
	@Email(message = "no es una dirección de correo bien formada")
	private String email;
	
	@JsonIgnore // Como las propiedades LOB son muy grandes y contienen mucha información, con esta anotación ignoramos esta propiedad a la hora de renderizar un objeto de esta clase entidad a un objeto Json para ser enviado en una respuesta de una petición http
	@Lob // Esta anotación nos permite persistir o almacenar objetos grandes, también llamados LOB's, en la base de datos, es decir, nos permite persistir o almacenar largas estructuras de información estructurada y no estructurada como texto, gráficos, audio y video(informácion multimedia) en la base de datos
	private byte[] foto;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP) // Con esta anotación podemos indicar si quqeremos guardar en el campo "create_at" de la tabla sólo la fecha(TemporalType.DATE), solo el tiempo(TemporalType.TIME) o ambas cosas(TemporalType.TIMESTAMP)
	private Date createAt;
	
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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	
	// Este método es para devolver el HashCode o identificador único de la foto del alumno
	// Como el nombre de este método empieza por "get", el HashCode devuelto por este método también se va a renderizar en el objeto Json de la respuesta de la petición http usando, como nombre de la propiedad, el resto del nombre de este método comenzando en minúscula, es decir, "fotoHashCode"
	public Integer getFotoHashCode() {
		// Si el alumno no tiene foto, devolvemos null y, en caso contrario, devolvemos el HashCode de la foto
		return this.foto != null ? this.foto.hashCode() : null;
	}

	// Sobrescribimos este método para poder implementar nuestra comparación de objetos Alumno y así poder eliminar con éxito alumnos del ArrayList de alumnos de la clase entidad Curso
	// Es decir, cuando se invoca al método "remove" de un ArrayList, se invoca por debajo el método "equals" de los objetos contenidos en ese ArrayList para ver si alguno de ellos es igual al objeto que se le pasa al método "remove" para su eliminación
	@Override
	public boolean equals(Object obj) {
		
		// Si ambos objeto tienen la misma referencia, devolvemos true para indicar que son iguales
		if(this == obj)
			return true;
		
		// Si el objeto a comparar no es una instancia de Alumno, devolvemos false porque no es un tipo de objeto válido para comparar
		if(!(obj instanceof Alumno))
			return false;
		
		// En caso contrario, si el objeto a comparar sí es una instancia de Alumno, devolvemos el resultado de comprobar sus ids
		Alumno a = (Alumno)obj;
		return this.id != null && this.id.equals(a.getId());
	}
	
	
	
}
