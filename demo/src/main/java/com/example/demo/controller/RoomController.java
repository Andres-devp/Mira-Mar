package com.example.demo.controller;

import com.example.demo.entities.Room;
import com.example.demo.service.HabitacionService;
import com.example.demo.service.TipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private TipoHabitacionService tipoHabitacionService;

    @GetMapping
    public String listRooms(@RequestParam(required = false) Integer capacidad,
                            @RequestParam(required = false) Double precioMax,
                            Model model) {
        model.addAttribute("tipos", tipoHabitacionService.filtrarTipos(capacidad, precioMax));
        model.addAttribute("capacidadFiltro", capacidad);
        model.addAttribute("precioMaxFiltro", precioMax);
        return "rooms/rooms-list";
    }

    @GetMapping("/table")
    public String listRoomsTable(Model model) {
        List<Room> habitaciones = habitacionService.getAllHabitaciones();
        model.addAttribute("rooms", habitaciones);
        return "rooms/rooms-table";
    }

    @GetMapping("/{id}")
    public String roomDetail(@PathVariable Long id, Model model) {
        model.addAttribute("tipo", tipoHabitacionService.getTipoById(id));
        return "rooms/room-detail";
    }

    @GetMapping("/add")
    public String showCreateForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("isNew", true);
        model.addAttribute("roomTypes", tipoHabitacionService.getAllTipos());
        return "rooms/room-form";
    }

    @PostMapping
    public String createRoom(@ModelAttribute Room room) {
        habitacionService.saveHabitacion(room);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Room habitacion = habitacionService.getHabitacionById(id);
        model.addAttribute("room", habitacion);
        model.addAttribute("isNew", false);
        model.addAttribute("roomTypes", tipoHabitacionService.getAllTipos());
        return "rooms/room-form";
    }

    @PostMapping("/{id}")
    public String updateRoom(@PathVariable Long id, @ModelAttribute Room room) {
        room.setId(id);
        habitacionService.saveHabitacion(room);
        return "redirect:/rooms/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteRoom(@PathVariable Long id) {
        habitacionService.deleteHabitacion(id);
        return "redirect:/rooms";
    }
}
