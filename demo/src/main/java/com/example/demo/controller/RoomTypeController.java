package com.example.demo.controller;

import com.example.demo.entities.RoomType;
import com.example.demo.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/roomtypes")
public class RoomTypeController {

    @Autowired
    private RoomTypeService tipoHabitacionService;

    @GetMapping({"", "/table"})
    public String listTypes(Model model) {
        List<RoomType> tipos = tipoHabitacionService.getAllTipos();
        model.addAttribute("tiposHabitacion", tipos);
        return "rooms/roomtype-tabla";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("tipo", new RoomType());
        return "rooms/roomtype-form";
    }

    @PostMapping("/add")
    public String saveType(@ModelAttribute("tipo") RoomType tipo) {
        tipoHabitacionService.saveTipo(tipo);
        return "redirect:/roomtypes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        RoomType tipo = tipoHabitacionService.getTipoById(id);
        model.addAttribute("tipo", tipo);
        return "rooms/roomtype-form";
    }

    @PostMapping("/edit/{id}")
    public String updateType(@PathVariable Long id, @ModelAttribute("tipo") RoomType tipo) {
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
