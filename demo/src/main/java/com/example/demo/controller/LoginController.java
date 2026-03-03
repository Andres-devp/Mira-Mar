package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // Importante añadir esta

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; 
    }

    
    @PostMapping("/login")
    public String procesarLogin() {
        return "redirect:/index"; 
    }
}