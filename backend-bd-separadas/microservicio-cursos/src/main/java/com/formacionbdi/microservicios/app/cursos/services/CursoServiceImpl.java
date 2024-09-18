package com.formacionbdi.microservicios.app.cursos.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.cursos.clients.AlumnoFeignClient;
import com.formacionbdi.microservicios.app.cursos.clients.RespuestaFeignClient;
import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.app.cursos.models.repository.CursoRepository;
import com.formacionbdi.microservicios.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commons.services.CommonServiceImpl;

@Service
public class CursoServiceImpl extends CommonServiceImpl<Curso,CursoRepository> implements CursoService{

	@Autowired
	private AlumnoFeignClient alumnoClient;
	
	@Autowired
	private RespuestaFeignClient respuestaClient;
	
	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public Curso findCursoByAlumnoId(Long id) {
		return this.repository.findCursoByAlumnoId(id);
	}

	@Override
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(Long alumnoId) {
		return respuestaClient.obtenerExamenesIdsConRespuestasAlumno(alumnoId);
	}

	@Override
	public Iterable<Alumno> obtenerAlumnosPorCurso(Iterable<Long> ids) {
		return alumnoClient.obtenerAlumnosPorCurso(ids);
	}

	@Transactional // No ponemos "readOnly = true" porque se va a modificar una tabla de la base de datos
	@Override
	public void eliminarCursoAlumnoPorId(Long alumnoId) {
		this.repository.eliminarCursoAlumnoPorId(alumnoId);
		
	}

}
