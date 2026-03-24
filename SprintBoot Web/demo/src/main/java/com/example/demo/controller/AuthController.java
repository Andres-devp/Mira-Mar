package com.example.demo.controller;

import com.example.demo.exception.RegistrationException;
import com.example.demo.service.AuthenticatedUser;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
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
                                HttpSession session,
                                RedirectAttributes flash) {
        AuthenticatedUser usuarioAutenticado = authService.autenticar(username, password);
        if (usuarioAutenticado == null) {
            flash.addFlashAttribute("error", "Usuario o contraseña incorrectos");
            return "redirect:/login-page";
        }

        session.setAttribute("usuarioId", usuarioAutenticado.getId());
        session.setAttribute("usuarioRol", usuarioAutenticado.getRol());

        if ("ADMIN".equals(usuarioAutenticado.getRol())) {
            return "redirect:/admin";
        }
        if ("OPERATOR".equals(usuarioAutenticado.getRol())) {
            return "redirect:/operator";
        }
        return "redirect:/usuarios/" + usuarioAutenticado.getId();
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
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
