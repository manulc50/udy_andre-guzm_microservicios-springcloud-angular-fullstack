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
public class ExamenServiceImpl extends CommonServiceImpl<Examen, ExamenRepository> implements ExamenService {

	@Autowired
	private AsignaturaRepository asignaturaRepository;
	
	@Override
	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	public List<Examen> findByNombre(String termino) {
		return this.repository.findByNombre(termino);
	}

	@Override
	@Transactional(readOnly = true) // Ponemos "readOnly = true" porque sólo se va a realizar una consulta a la base de datos
	public Iterable<Asignatura> findAllAsignaturas() {
		return this. asignaturaRepository.findAll();
	}

}
