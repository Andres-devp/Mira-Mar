package com.example.demo.controller;

import com.example.demo.entities.Reservation;
import com.example.demo.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Reservaciones", description = "Gestión de reservaciones")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping({"/all", ""})
	@Operation(summary = "Listar todas las reservaciones")
	public List<Reservation> listReservations() {
		return reservationService.getAllReservas();
	}

	@GetMapping("/find/{id}")
	@Operation(summary = "Buscar reservación por ID")
	public Reservation findById(@PathVariable Long id) {
		return reservationService.getReservaById(id);
	}

	@PostMapping("/add")
	@Operation(summary = "Crear nueva reservación")
	public Reservation addReservation(@RequestBody Reservation reservation) {
		return reservationService.saveReserva(reservation);
	}

	@PutMapping("/update/{id}")
	@Operation(summary = "Actualizar reservación existente")
	public Reservation updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
		reservation.setId(id);
		return reservationService.saveReserva(reservation);
	}

	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Eliminar reservación por ID")
	public void deleteReservation(@PathVariable Long id) {
		reservationService.deleteReserva(id);
	}
}
