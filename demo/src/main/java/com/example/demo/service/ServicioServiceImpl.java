package com.example.demo.service;

import com.example.demo.entities.Servicio;
import com.example.demo.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioServiceImpl implements ServicioService {
	private final ServicioRepository servicioRepository;

	public ServicioServiceImpl(ServicioRepository servicioRepository) {
		this.servicioRepository = servicioRepository;
	}

	@Override
	public List<Servicio> getAllServicios() {
		return servicioRepository.findAll();
	}

	@Override
	public Optional<Servicio> getServicioById(Long id) {
		return servicioRepository.findById(id);
	}

	@Override
	public Servicio addServicio(Servicio servicio) {
		return servicioRepository.addServicio(servicio);
	}

	@Override
	public Servicio updateServicio(Long id, Servicio servicio) {
		return servicioRepository.updateServicio(id, servicio);
	}

	@Override
	public boolean deleteServicio(Long id) {
		return servicioRepository.deleteServicio(id);
	}
}
