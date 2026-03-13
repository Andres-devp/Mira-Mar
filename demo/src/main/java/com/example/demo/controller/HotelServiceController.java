package com.example.demo.controller;

import com.example.demo.entities.HotelService;
import com.example.demo.service.HotelServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/services")
public class HotelServiceController {

	@Autowired
	private HotelServiceService servicioService;

	@GetMapping
	public String listServicios() {
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
		return "HotelServices/services-list";
	}

	@GetMapping("/{id}")
	public String servicioDetail(@PathVariable Long id, Model model) {
		HotelService servicio = servicioService.getServicioById(id);
		model.addAttribute("servicio", servicio);
		return "HotelServices/service-detail";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("servicio", new HotelService());
		return "HotelServices/service-form";
	}

	@PostMapping("/add")
	public String addServicio(@ModelAttribute HotelService servicio) {
		servicioService.addServicio(servicio);
		return "redirect:/services";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		HotelService servicio = servicioService.getServicioById(id);
		model.addAttribute("servicio", servicio);
		return "HotelServices/service-form";
	}

	@PostMapping("/edit/{id}")
	public String editServicio(@PathVariable Long id, @ModelAttribute HotelService servicio) {
		servicioService.updateServicio(id, servicio);
		return "redirect:/services";
	}

	@PostMapping("/delete/{id}")
	public String deleteServicio(@PathVariable Long id) {
		servicioService.deleteServicio(id);
		return "redirect:/services";
	}
}
