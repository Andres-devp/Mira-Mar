package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.CreateReservationRequest;
import com.example.demo.entities.Reservation;
import com.example.demo.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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

	@GetMapping("/{id}")
	@Operation(summary = "Buscar reservación por ID")
	public Reservation findById(@PathVariable Long id) {
		return reservationService.getReservaById(id);
	}

	@PostMapping("/add")
	@Operation(summary = "Crear nueva reservación y asignar habitación disponible")
	public ResponseEntity<?> addReservation(
		@CookieValue(value = "user_session", required = false) String userSession,
		@RequestBody CreateReservationRequest request
	) {
		if (userSession == null || userSession.isBlank()) {
			return ResponseEntity.status(HttpStatus.FOUND)
				.header(HttpHeaders.LOCATION, "http://localhost:4200/login")
				.build();
		}

		Long sessionUserId;
		try {
			sessionUserId = Long.valueOf(userSession);
		} catch (NumberFormatException ex) {
			return ResponseEntity.status(HttpStatus.FOUND)
				.header(HttpHeaders.LOCATION, "http://localhost:4200/login")
				.build();
		}

		Reservation reservation = reservationService.createReserva(sessionUserId, request);
		return ResponseEntity.ok(reservation);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar reservación existente")
	public Reservation updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
		reservation.setId(id);
		return reservationService.saveReserva(reservation);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar reservación por ID")
	public void deleteReservation(@PathVariable Long id) {
		reservationService.deleteReserva(id);
	}
}
