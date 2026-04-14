package com.example.demo.controller;

import com.example.demo.entities.HotelService;
import com.example.demo.service.HotelServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

	@GetMapping("/find/{id}")
	@Operation(summary = "Buscar servicio por ID")
	public HotelService findById(@PathVariable Long id) {
		return servicioService.getServicioById(id);
	}

	@PostMapping("/add")
	@Operation(summary = "Agregar nuevo servicio")
	public HotelService addServicio(@RequestBody HotelService servicio) {
		return servicioService.addServicio(servicio);
	}

	@PutMapping("/update/{id}")
	@Operation(summary = "Actualizar servicio existente")
	public HotelService updateServicio(@PathVariable Long id, @RequestBody HotelService servicio) {
		return servicioService.updateServicio(id, servicio);
	}

	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Eliminar servicio por ID")
	public void deleteServicio(@PathVariable Long id) {
		servicioService.deleteServicio(id);
	}
}
