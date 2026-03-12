package com.example.demo.service;

import com.example.demo.entities.Servicio;

import java.util.List;

public interface ServicioService {
	List<Servicio> getAllServicios();

	Servicio getServicioById(Long id);

	Servicio addServicio(Servicio servicio);

	Servicio updateServicio(Long id, Servicio servicio);

	void deleteServicio(Long id);
}
