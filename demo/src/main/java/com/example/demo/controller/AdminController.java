package com.example.demo.controller;

import com.example.demo.service.HabitacionService;
import com.example.demo.service.RoomTypeService;
import com.example.demo.service.ServicioService;
import com.example.demo.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioService usuarioService;
    private final ServicioService servicioService;
    private final HabitacionService habitacionService;
    private final RoomTypeService roomTypeService;

    public AdminController(UsuarioService usuarioService, ServicioService servicioService, HabitacionService habitacionService, RoomTypeService roomTypeService) {
        this.usuarioService = usuarioService;
        this.servicioService = servicioService;
        this.habitacionService = habitacionService;
        this.roomTypeService = roomTypeService;
    }

    @GetMapping({"", "/"})
    public String admin() {
        return "admin";
    }

    @GetMapping("/usuarios")
    public String usuariosTabla(Model model) {
        model.addAttribute("usuarios", usuarioService.searchAll());
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
        model.addAttribute("roomTypes", roomTypeService.getAllRoomTypes());
        return "rooms/roomtype-tabla";
    }
}
