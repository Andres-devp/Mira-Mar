package com.example.demo.service;

import com.example.demo.entities.Servicio;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

	@Autowired
	private ServicioRepository servicioRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Servicio> getAllServicios() {
		return servicioRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Servicio getServicioById(Long id) {
		return servicioRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("No se encontró servicio con ID: " + id, id));
	}

	@Override
	public Servicio addServicio(Servicio servicio) {
		return servicioRepository.save(servicio);
	}

	@Override
	public Servicio updateServicio(Long id, Servicio servicio) {
		if (!servicioRepository.existsById(id))
			throw new NotFoundException("No se encontró servicio con ID: " + id, id);
		servicio.setId(id);
		return servicioRepository.save(servicio);
	}

	@Override
	public void deleteServicio(Long id) {
		if (!servicioRepository.existsById(id))
			throw new NotFoundException("No se encontró servicio con ID: " + id, id);
		servicioRepository.deleteById(id);
	}
}

