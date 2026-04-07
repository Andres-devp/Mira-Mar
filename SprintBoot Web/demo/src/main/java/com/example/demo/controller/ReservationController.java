package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entities.Reservation;
import com.example.demo.service.ReservationService;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@GetMapping
	public String listReservations() {
		return "redirect:/reservations/table";
	}

	@GetMapping("/table")
	public String listReservationsTable(Model model) {
		model.addAttribute("reservations", reservationService.getAllReservas());
		return "reservations/reservations-table";
	}

	@GetMapping("/cards")
	public String listReservationsCards(Model model) {
		model.addAttribute("reservations", reservationService.getAllReservas());
		return "reservations/reservations-list";
	}

	@GetMapping("/{id}")
	public String reservationDetail(@PathVariable Long id, Model model) {
		Reservation reservation = reservationService.getReservaById(id);
		model.addAttribute("reservation", reservation);
		return "reservations/reservation-detail";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("reservation", new Reservation());
		return "reservations/reservation-form";
	}

	@PostMapping("/add")
	public String addReservation(@ModelAttribute Reservation reservation) {
		reservationService.saveReserva(reservation);
		return "redirect:/reservations/table";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Reservation reservation = reservationService.getReservaById(id);
		model.addAttribute("reservation", reservation);
		return "reservations/reservation-form";
	}

	@PostMapping("/edit/{id}")
	public String editReservation(@PathVariable Long id, @ModelAttribute Reservation reservation) {
		reservation.setId(id);
		reservationService.saveReserva(reservation);
		return "redirect:/reservations/table";
	}

	@PostMapping("/delete/{id}")
	public String deleteReservation(@PathVariable Long id) {
		reservationService.deleteReserva(id);
		return "redirect:/reservations/table";
	}
}
