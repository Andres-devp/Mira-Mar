package com.example.demo.controller;

import com.example.demo.entities.TipoHabitacion;
import com.example.demo.service.TipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/roomtypes")
public class RoomTypeController {

    @Autowired
    private TipoHabitacionService tipoHabitacionService;

    @GetMapping({"", "/table"})
    public String listTypes(Model model) {
        List<TipoHabitacion> tipos = tipoHabitacionService.getAllTipos();
        model.addAttribute("tiposHabitacion", tipos);
        return "rooms/roomtype-tabla";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("tipo", new TipoHabitacion());
        return "rooms/roomtype-form";
    }

    @PostMapping("/add")
    public String saveType(@ModelAttribute("tipo") TipoHabitacion tipo) {
        tipoHabitacionService.saveTipo(tipo);
        return "redirect:/roomtypes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        TipoHabitacion tipo = tipoHabitacionService.getTipoById(id);
        model.addAttribute("tipo", tipo);
        return "rooms/roomtype-form";
    }

    @PostMapping("/edit/{id}")
    public String updateType(@PathVariable Long id, @ModelAttribute("tipo") TipoHabitacion tipo) {
        tipo.setId(id);
        tipoHabitacionService.saveTipo(tipo);
        return "redirect:/roomtypes";
    }

    @PostMapping("/delete/{id}")
    public String deleteType(@PathVariable Long id) {
        tipoHabitacionService.deleteTipo(id);
        return "redirect:/roomtypes";
    }
}
