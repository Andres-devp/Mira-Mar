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
            return "redirect:/dashboard";
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

    @PostMapping("/create-account")
    public String procesarCrearCuenta(@RequestParam String nombre,
                                    @RequestParam String usuario,
                                    @RequestParam String email,
                                    @RequestParam String contrasena,
                                    @RequestParam String contrasenaConfirm,
                                    RedirectAttributes flash) {
        // Validar que las contraseñas coincidan
        if (!contrasena.equals(contrasenaConfirm)) {
            flash.addFlashAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/create-account";
        }

        // Validar que el usuario no exista
        Usuario usuarioExistente = usuarioService.searchByUsername(usuario);
        if (usuarioExistente != null) {
            flash.addFlashAttribute("error", "El nombre de usuario ya está en uso");
            return "redirect:/create-account";
        }

        // Crear nuevo usuario con rol CLIENTE por defecto
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setUsuario(usuario);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setContrasena(contrasena);
        nuevoUsuario.setRol("CLIENTE");
        nuevoUsuario.setTelefono(""); // vacío por defecto
        nuevoUsuario.setFotoPerfil(null); // sin foto de perfil por defecto

        // Guardar el usuario
        usuarioService.save(nuevoUsuario);

        // Redirigir al login con mensaje de éxito
        flash.addFlashAttribute("mensaje", "Cuenta creada exitosamente. Por favor inicia sesión.");
        return "redirect:/login";
    }
}
