package com.example.demo.service;

import com.example.demo.entities.Servicio;
import com.example.demo.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {
	private final ServicioRepository servicioRepository;

	public ServicioServiceImpl(ServicioRepository servicioRepository) {
		this.servicioRepository = servicioRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Servicio> getAllServicios() {
		return servicioRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Servicio> getServicioById(Long id) {
		return servicioRepository.findById(id);
	}

	@Override
	public Servicio addServicio(Servicio servicio) {
		return servicioRepository.save(servicio);
	}

	@Override
	public Servicio updateServicio(Long id, Servicio servicio) {
		if (!servicioRepository.existsById(id)) return null;
		servicio.setId(id);
		return servicioRepository.save(servicio);
	}

	@Override
	public boolean deleteServicio(Long id) {
		if (!servicioRepository.existsById(id)) return false;
		servicioRepository.deleteById(id);
		return true;
	}
}

