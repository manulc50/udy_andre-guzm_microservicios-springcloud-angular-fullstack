package com.formacionbdi.microservicios.commons.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Interfaz de la capa de servicio genérica para todos los microservicios
// "E" representa una entidad genérica de nuestro modelo(Alumno, Curso, Exámen, etc...)

public interface CommonService<E> {
	
	// También es posible devolver directamente una interfaz List<E>, pero la interfaz Iterable<E> es más genérica
	public Iterable<E> findAll();
	
	public Page<E> findAll(Pageable pageable);
	
	// También es posible devolver directamente un objeto de Tipo "E", pero, donde invoquemos a este método, depués tenemos que comprobar a parte si ese objeto es nulo(significa que la entidad no existe en el sistema) o si viene definido(significa que la entidad sí existe) y hacer el tratamiento correspondiente. Sin embargo, el objeto "Optional" integra este tipo de comprobación directamente y, además, también podemos lanzar de excepciones directamente desde este objeto si hay valores nulos
	public Optional<E> findById(Long id);
	
	public E save(E entity);
	
	public void deleteById(Long id);
}
