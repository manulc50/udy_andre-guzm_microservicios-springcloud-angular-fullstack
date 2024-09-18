package com.formacionbdi.microservicios.app.respuestas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;
import com.formacionbdi.microservicios.app.respuestas.models.repositoty.RespuestaRepository;

@Service
public class RespuestaServiceImpl implements RespuestaService {

	@Autowired
	private RespuestaRepository respuestaRepository;
	
	// @Autowired
	// private ExamenFeignClient examenClient;
	
	// No ponemos la anotación @Transactional porque las bases de datos MongoDB de tipo NoSQL no son bases de datos transaccionales
	@Override
	public Iterable<Respuesta> saveAll(Iterable<Respuesta> respuestas) {
		return respuestaRepository.saveAll(respuestas);
	}

	// No ponemos la anotación @Transactional porque las bases de datos MongoDB de tipo NoSQL no son bases de datos transaccionales
	@Override
	public Iterable<Respuesta> findRespuestasByAlumnoByExamen(Long alumnoId, Long examenId) {
		/*Examen examen = examenClient.obtenerExamenPorId(examenId);
		List<Pregunta> preguntas = examen.getPreguntas();
		List<Long> preguntaIds = preguntas.stream().map(p -> p.getId()).collect(Collectors.toList());
		List<Respuesta> respuestas = (List<Respuesta>) respuestaRepository.findRespuestaByAlumnoByPreguntaIds(alumnoId, preguntaIds);
		respuestas.forEach(r -> System.out.println(r.getTexto()));
		respuestas = respuestas.stream().map(r -> {
			preguntas.forEach(p -> {
				if(p.getId() == r.getPreguntaId())
					r.setPregunta(p);
			});	
			return r;
		})
		.collect(Collectors.toList());*/
		
		// Comentamos lo de arriba porque ahora tenemos el exámen almacenado en la base de datos MongoDB que utiliza este microservicio "microservicio-repuestas" y ya no hace falta realizar una petición http al microservicio "microservicio-examenes", mediante un cliente Feign, para obtener el exámen
		// Esta alternativa es mejor hablando en términos de rendimiento porque nos ahorramos una llamada o comunicación con otro microservicio("examenClient.obtenerExamenPorId(examenId)")
		List<Respuesta> respuestas = (List<Respuesta>) respuestaRepository.findRespuestaByAlumnoByExamen(alumnoId, examenId);
		return respuestas;
	}

	// No ponemos la anotación @Transactional porque las bases de datos MongoDB de tipo NoSQL no son bases de datos transaccionales
	@Override
	public Iterable<Long> findExamenesIdsConRespuestasByAlumno(Long alumnoId) {
		/*List<Respuesta> respuestas = (List<Respuesta>) respuestaRepository.findByAlumnoId(alumnoId);
		List<Long> examenIds = Collections.emptyList();
		if(respuestas != null && respuestas.size() > 0) {
			List<Long> preguntaIds = respuestas.stream().map(r -> r.getPreguntaId()).collect(Collectors.toList());
			examenIds = (List<Long>)examenClient.obtenersExamenesIdsPorPreguntasIds(preguntaIds);
		}*/
		
		// Comentamos lo de arriba porque ahora tenemos el exámen almacenado en la base de datos MongoDB que utiliza este microservicio "microservicio-repuestas" y ya no hace falta realizar una petición http al microservicio "microservicio-examenes", mediante un cliente Feign, para obtener los ids de los exámenes respondidos por el alumno
		// Esta alternativa es mejor hablando en términos de rendimiento porque nos ahorramos una llamada o comunicación con otro microservicio("examenClient.obtenersExamenesIdsPorPreguntasIds(preguntaIds)")
		// Como la consulta realizada por el método "findExamenesIdsConRespuestasByAlumno" del repositorio "respuestaRepository" devuelve, para cada respuesta del alumno, el id del exámen que contiene la pregunta de esa respuesta, tenemos que usar el método "distinct" sobre el stream de ids de exámenes porque esos ids van a estar repetidos y los queremos agrupar
		List<Respuesta> respuestas = (List<Respuesta>) respuestaRepository.findExamenesIdsConRespuestasByAlumno(alumnoId);
		List<Long> examenIds = respuestas.stream().map(r -> r.getPregunta().getExamen().getId()).distinct().collect(Collectors.toList());
		return examenIds;
	}

	// No ponemos la anotación @Transactional porque las bases de datos MongoDB de tipo NoSQL no son bases de datos transaccionales
	@Override
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId) {
		return respuestaRepository.findByAlumnoId(alumnoId);
	}

}
