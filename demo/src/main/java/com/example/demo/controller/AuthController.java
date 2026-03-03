package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entities.Usuario;
import com.example.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                RedirectAttributes flash,
                                HttpSession session) {
        Usuario usuario = usuarioService.searchByUsername(username);
        if (usuario != null && usuario.getContrasena().equals(password)) {
            // credenciales válidas; guardar en sesión para posteriores comprobaciones
            session.setAttribute("loggedUser", usuario);
            return "redirect:/index";
        }
        // falló autenticación
        flash.addFlashAttribute("error", "Usuario o contraseña incorrectos");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/create-account")
    public String createAccount() {
        return "create-account";
    }
}
