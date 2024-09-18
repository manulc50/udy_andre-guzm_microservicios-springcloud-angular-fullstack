package com.formacionbdi.microservicios.commons.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

// Implementación de la interfaz de la capa de servicio genérica para todos los microservicios

// "E" representa una entidad genérica de nuestro modelo(Puede ser Alumno, Curso, Exámen, etc...)
// "R" es una interfaz que extiende de la interfaz PagingAndSortingRepository de Spring Data JPA y representa la capa Dao o el repositorio genérico para interacturar con la base de datos usando las entidades genéricas "E"

public class CommonServiceImpl<E,R extends PagingAndSortingRepository<E,Long>> implements CommonService<E> {
	
	@Autowired
	protected R repository; // Usamos la restricción "protected" para que esta propiedad pueda ser usada por las clases hijas que hereden esta clase padre "CommonServiceImpl<E>"

	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public Iterable<E> findAll() {
		return repository.findAll();
	}
	
	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public Page<E> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public Optional<E> findById(Long id) {
		return repository.findById(id);
	}

	@Transactional // No ponemos "readOnly = true" porque se va a modificar una tabla de la base de datos
	@Override
	public E save(E entidad) {
		// Con el método "save", si la entidad no existe en la base de datos, la persiste o la guarda, y si esa entidad ya existe, hace un merge o la actualiza
		return repository.save(entidad);
	}

	@Transactional // No ponemos "readOnly = true" porque se va a modificar una tabla de la base de datos
	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);

	}

}
