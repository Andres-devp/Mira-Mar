package com.example.demo.service;

import com.example.demo.entities.Servicio;

import java.util.List;
import java.util.Optional;

public interface ServicioService {
	List<Servicio> getAllServicios();

	Optional<Servicio> getServicioById(Long id);

	Servicio addServicio(Servicio servicio);

	Servicio updateServicio(Long id, Servicio servicio);

	boolean deleteServicio(Long id);
}
