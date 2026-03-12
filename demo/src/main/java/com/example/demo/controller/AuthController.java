package com.example.demo.controller;

import com.example.demo.entities.Cliente;
import com.example.demo.exception.RegistrationException;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login-page")
    public String login() {
        return "Usuarios/login/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                RedirectAttributes flash) {
        Cliente cliente = authService.autenticar(username, password);
        if (cliente == null) {
            flash.addFlashAttribute("error", "Usuario o contraseña incorrectos");
            return "redirect:/login-page";
        }
        if ("ADMIN".equals(cliente.getRol())) {
            return "redirect:/admin";
        }
        return "redirect:/usuarios/" + cliente.getId();
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/create-account")
    public String createAccount() {
        return "Usuarios/login/create-account";
    }

    @PostMapping("/create-account")
    public String procesarCrearCuenta(@RequestParam String nombre,
                                      @RequestParam String usuario,
                                      @RequestParam String email,
                                      @RequestParam String contrasena,
                                      @RequestParam String contrasenaConfirm,
                                      RedirectAttributes flash) {
        try {
            authService.registrar(nombre, usuario, email, contrasena, contrasenaConfirm);
            flash.addFlashAttribute("mensaje", "Cuenta creada exitosamente. Por favor inicia sesión.");
            return "redirect:/login-page";
        } catch (RegistrationException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/create-account";
        }
    }
}
