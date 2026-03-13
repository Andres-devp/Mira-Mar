package com.example.demo.controller;

import com.example.demo.service.ClientService;
import com.example.demo.service.RoomService;
import com.example.demo.service.HotelServiceService;
import com.example.demo.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ClientService clienteService;

    @Autowired
    private HotelServiceService servicioService;

    @Autowired
    private RoomService habitacionService;

    @Autowired
    private RoomTypeService tipoHabitacionService;

    @GetMapping({"", "/"})
    public String admin() {
        return "admin";
    }

    @GetMapping("/usuarios")
    public String usuariosTabla(Model model) {
        model.addAttribute("usuarios", clienteService.getAllClientes());
        return "Usuarios/usuarios-tabla";
    }

    @GetMapping("/servicios")
    public String serviciosTabla(Model model) {
        model.addAttribute("servicios", servicioService.getAllServicios());
        return "HotelServices/services-table";
    }

    @GetMapping("/habitaciones")
    public String habitacionesTabla(Model model) {
        model.addAttribute("rooms", habitacionService.getAllHabitaciones());
        return "rooms/rooms-table";
    }

    @GetMapping("/tipos-habitacion")
    public String tiposHabitacionTabla(Model model) {
        model.addAttribute("tiposHabitacion", tipoHabitacionService.getAllTipos());
        return "rooms/roomtype-tabla";
    }
}
