package com.example.demo.service;

import com.example.demo.entities.HotelService;

import java.util.List;

public interface ServicioService {
	List<HotelService> getAllServicios();

	HotelService getServicioById(Long id);

	HotelService addServicio(HotelService servicio);

	HotelService updateServicio(Long id, HotelService servicio);

	void deleteServicio(Long id);
}
