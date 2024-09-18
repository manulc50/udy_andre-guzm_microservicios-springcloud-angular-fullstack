package com.formacionbdi.microservicios.app.examenes.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.examenes.models.repository.AsignaturaRepository;
import com.formacionbdi.microservicios.app.examenes.models.repository.ExamenRepository;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Asignatura;
import com.formacionbdi.microservicios.commons.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.commons.services.CommonServiceImpl;

@Service
public class ExamenServiceImpl extends CommonServiceImpl<Examen,ExamenRepository> implements ExamenService{

	@Autowired
	private AsignaturaRepository asignaturaRepository;
	
	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public List<Examen> findByNombre(String termino) {
		return this.repository.findByNombre(termino);
	}

	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public Iterable<Asignatura> findAllAsignaturas() {
		return asignaturaRepository.findAll();
	}

	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	@Override
	public Iterable<Long> findExamenesIdsByPreguntaIds(Iterable<Long> preguntaIds) {
		return this.repository.findExamenesIdsByPreguntaIds(preguntaIds);
	}
	
	

}
