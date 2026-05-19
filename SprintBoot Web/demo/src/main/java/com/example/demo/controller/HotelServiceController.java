package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.demo.entities.HotelService;
import com.example.demo.service.HotelServiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Servicios del Hotel", description = "Gestión de servicios del hotel")
public class HotelServiceController {

	@Autowired
	private HotelServiceService servicioService;

	@GetMapping({"/all", ""})
	@Operation(summary = "Listar todos los servicios del hotel")
	public List<HotelService> listServicios() {
		return servicioService.getAllServicios();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar servicio por ID")
	public HotelService findById(@PathVariable Long id) {
		return servicioService.getServicioById(id);
	}

	@PostMapping("/add")
	@Operation(summary = "Agregar nuevo servicio")
	@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
	public HotelService addServicio(@RequestBody HotelService servicio) {
		return servicioService.addServicio(servicio);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar servicio existente")
	@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
	public HotelService updateServicio(@PathVariable Long id, @RequestBody HotelService servicio) {
		return servicioService.updateServicio(id, servicio);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar servicio por ID")
	@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
	public void deleteServicio(@PathVariable Long id) {
		servicioService.deleteServicio(id);
	}
}
