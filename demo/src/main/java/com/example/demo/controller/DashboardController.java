package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entities.Usuario;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Verificar si hay un usuario autenticado
        Usuario loggedUser = (Usuario) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            // Si no hay usuario autenticado, redirigir a login
            return "redirect:/login";
        }
        
        // Pasar el usuario autenticado al modelo
        model.addAttribute("loggedUser", loggedUser);
        return "dashboard";
    }
}
