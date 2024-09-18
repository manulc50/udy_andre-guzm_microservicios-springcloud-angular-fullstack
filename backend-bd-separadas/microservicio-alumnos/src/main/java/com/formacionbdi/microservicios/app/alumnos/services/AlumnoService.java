package com.formacionbdi.microservicios.app.alumnos.services;

import java.util.List;

import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.services.CommonService;

public interface AlumnoService extends CommonService<Alumno>{
	
	public List<Alumno> findByNombreOrApellidos(String termino);	
	public Iterable<Alumno> findAllById(Iterable<Long> ids);
	public void eliminarCursoAlumnoPorId(Long alumnoId);
	
}
