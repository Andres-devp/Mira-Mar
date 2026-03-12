package com.example.demo.controller;

import com.example.demo.service.TipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private TipoHabitacionService tipoHabitacionService;

    @GetMapping("/")
    public String root(Model model) {
        List<?> tipos = tipoHabitacionService.getAllTipos();
        model.addAttribute("tiposDestacados", tipos.stream().limit(3).toList());
        return "index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<?> tipos = tipoHabitacionService.getAllTipos();
        model.addAttribute("tiposDestacados", tipos.stream().limit(3).toList());
        return "index";
    }
}
