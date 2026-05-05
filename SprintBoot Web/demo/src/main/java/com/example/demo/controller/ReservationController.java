package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.example.demo.entities.Account;
import com.example.demo.entities.AccountItem;

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
	public ResponseEntity<List<Reservation>> listReservations() {
		return ResponseEntity.ok(reservationService.getAllReservas());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar reservación por ID")
	public ResponseEntity<Reservation> findById(@PathVariable Long id) {
		return ResponseEntity.ok(reservationService.getReservaById(id));
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
	public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
		reservation.setId(id);
		return ResponseEntity.ok(reservationService.saveReserva(reservation));
	}

	@PutMapping("/{id}/status")
	@Operation(summary = "Actualizar el estado de una reservación")
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
		String nuevoEstado = body.get("estado");
		if (nuevoEstado == null || nuevoEstado.isBlank()) {
			return ResponseEntity.badRequest().body(Map.of("error", "El estado es obligatorio"));
		}
		try {
			Reservation updated = reservationService.updateEstado(id, nuevoEstado);
			return ResponseEntity.ok(updated);
		} catch (IllegalStateException | IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar reservación por ID")
	public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
		reservationService.deleteReserva(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/items")
	@Operation(summary = "Listar servicios adicionales de una reserva")
	public ResponseEntity<List<AccountItem>> listItems(@PathVariable Long id) {
		return ResponseEntity.ok(reservationService.getItemsByReservacion(id));
	}

	@GetMapping("/{id}/items/paid")
	@Operation(summary = "Listar servicios pagados de una reserva")
	public ResponseEntity<List<AccountItem>> listPaidItems(@PathVariable Long id) {
		return ResponseEntity.ok(reservationService.getPaidItemsByReservacion(id));
	}

	@PostMapping("/{id}/items")
	@Operation(summary = "Agregar servicio adicional a una reserva")
	public ResponseEntity<?> addItem(@PathVariable Long id, @RequestBody Map<String, Object> body) {
		Long hotelServiceId = Long.valueOf(body.get("hotelServiceId").toString());
		Integer cantidad = Integer.valueOf(body.get("cantidad").toString());
		try {
			AccountItem item = reservationService.addItemToReservacion(id, hotelServiceId, cantidad);
			return ResponseEntity.ok(item);
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@PutMapping("/{id}/items/{itemId}")
	@Operation(summary = "Actualizar cantidad de un servicio en la cuenta")
	public ResponseEntity<?> updateItem(
		@PathVariable Long id,
		@PathVariable Long itemId,
		@RequestBody Map<String, Object> body
	) {
		Integer cantidad = Integer.valueOf(body.get("cantidad").toString());
		try {
			AccountItem item = reservationService.updateItemCantidad(itemId, cantidad);
			return ResponseEntity.ok(item);
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@DeleteMapping("/{id}/items/{itemId}")
	@Operation(summary = "Eliminar servicio adicional de la cuenta")
	public ResponseEntity<?> removeItem(@PathVariable Long id, @PathVariable Long itemId) {
		try {
			reservationService.removeItem(itemId);
			return ResponseEntity.noContent().build();
		} catch (IllegalStateException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@PutMapping("/{id}/update")
	@Operation(summary = "Actualizar reservación existente con validación de disponibilidad")
	public ResponseEntity<?> updateReservacion(@PathVariable Long id, @RequestBody Map<String, Object> body) {
		try {
			Long roomTypeId = Long.valueOf(body.get("roomTypeId").toString());
			LocalDate fechaInicio = LocalDate.parse(body.get("fechaInicio").toString());
			LocalDate fechaFin = LocalDate.parse(body.get("fechaFin").toString());
			Integer cantidadPersonas = Integer.valueOf(body.get("cantidadPersonas").toString());
			Reservation updated = reservationService.updateReservacion(id, roomTypeId, fechaInicio, fechaFin, cantidadPersonas);
			return ResponseEntity.ok(updated);
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@GetMapping("/{id}/account")
	@Operation(summary = "Obtener la cuenta asociada a una reserva")
	public ResponseEntity<Account> getAccount(@PathVariable Long id) {
		return reservationService.getAccountByReservacion(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/{id}/pay")
	@Operation(summary = "Pagar la cuenta de una reserva")
	public ResponseEntity<?> pay(@PathVariable Long id) {
		try {
			Account cuenta = reservationService.payAccount(id);
			return ResponseEntity.ok(cuenta);
		} catch (IllegalStateException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
}
