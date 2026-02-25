package com.example.demo.controller;

import com.example.demo.entities.Servicio;
import com.example.demo.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/services")
public class ServicioController {
	private final ServicioService servicioService;

	@Autowired
	public ServicioController(ServicioService servicioService) {
		this.servicioService = servicioService;
	}

	@GetMapping
	public String listServicios(Model model) {
		return "redirect:/services/cards";
	}

	@GetMapping("/table")
	public String listServiciosTable(Model model) {
		model.addAttribute("servicios", servicioService.getAllServicios());
		return "HotelServices/services-table";
	}

	@GetMapping("/cards")
	public String listServiciosCards(Model model) {
		model.addAttribute("servicios", servicioService.getAllServicios());
		return "HotelServices/services-cards-list";
	}

	@GetMapping("/{id}")
	public String servicioDetail(@PathVariable Long id, Model model) {
		Optional<Servicio> servicio = servicioService.getServicioById(id);
		if (servicio.isPresent()) {
			model.addAttribute("servicio", servicio.get());
			return "HotelServices/service-detail";
		}
		return "redirect:/services";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("servicio", new Servicio());
		return "HotelServices/service-form";
	}

	@PostMapping("/add")
	public String addServicio(@ModelAttribute Servicio servicio) {
		servicioService.addServicio(servicio);
		return "redirect:/services";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Optional<Servicio> servicio = servicioService.getServicioById(id);
		if (servicio.isPresent()) {
			model.addAttribute("servicio", servicio.get());
			return "HotelServices/service-form";
		} else {
			return "redirect:/services";
		}
	}

	@PostMapping("/edit/{id}")
	public String editServicio(@PathVariable Long id, @ModelAttribute Servicio servicio) {
		servicioService.updateServicio(id, servicio);
		return "redirect:/services";
	}

	@PostMapping("/delete/{id}")
	public String deleteServicio(@PathVariable Long id) {
		servicioService.deleteServicio(id);
		return "redirect:/services";
	}
}
