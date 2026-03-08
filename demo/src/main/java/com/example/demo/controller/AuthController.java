package com.example.demo.controller;

import java.util.Collection;

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

    @GetMapping("/login-page")
    public String login() {
        return "Usuarios/login/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                            @RequestParam String password,
                            RedirectAttributes flash,
                            HttpSession session) {
    Collection<Usuario> usuarios = usuarioService.searchAll();

    if (usuarios != null) {
        for (Usuario u : usuarios) {
            if (u != null && u.getUsuario() != null && u.getContrasena() != null
                    && u.getUsuario().equals(username)
                    && u.getContrasena().equals(password)) {

                session.setAttribute("usuarioLogueado", u);
                session.setAttribute("rol", u.getRol());

                return "redirect:/perfil";
            }
        }
    }

    // fallo autenticación
    flash.addFlashAttribute("error", "Usuario o contraseña incorrectos");
    return "redirect:/login-page";
    }

    @GetMapping("/logout")
        public String logout(jakarta.servlet.http.HttpSession session) {
        session.invalidate();
    return "redirect:/login-page";
}

    @GetMapping("/create-account")
    public String createAccount() {
        return "Usuarios/login/create-account";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/login-page";
    }

    return "redirect:/usuarios/" + usuarioLogueado.getId();
}


@PostMapping("/create-account")
public String procesarCrearCuenta(@RequestParam String nombre,
                                  @RequestParam String usuario,
                                  @RequestParam String email,
                                  @RequestParam String contrasena,
                                  @RequestParam String contrasenaConfirm,
                                  RedirectAttributes flash) {

    // Limpiar espacios en blanco en los campos de texto
    nombre = nombre != null ? nombre.trim() : "";
    usuario = usuario != null ? usuario.trim() : "";
    email = email != null ? email.trim() : "";

    // Validar que todos los campos obligatorios hayan sido diligenciados
    if (nombre.isEmpty() || usuario.isEmpty() || email.isEmpty()
            || contrasena == null || contrasena.isEmpty()
            || contrasenaConfirm == null || contrasenaConfirm.isEmpty()) {
        flash.addFlashAttribute("error", "Todos los campos son obligatorios");
        return "redirect:/create-account";
    }

    // Validar que la contraseña y su confirmación coincidan
    if (!contrasena.equals(contrasenaConfirm)) {
        flash.addFlashAttribute("error", "Las contraseñas no coinciden");
        return "redirect:/create-account";
    }

    // Verificar que el nombre de usuario no se encuentre registrado
    Usuario usuarioExistente = usuarioService.searchByUsername(usuario);
    if (usuarioExistente != null) {
        flash.addFlashAttribute("error", "El nombre de usuario ya está en uso");
        return "redirect:/create-account";
    }

    // Verificar que el correo no se encuentre registrado
    Usuario emailExistente = usuarioService.searchByEmail(email);
    if (emailExistente != null) {
        flash.addFlashAttribute("error", "El correo electrónico ya está registrado");
        return "redirect:/create-account";
    }

    // Crear el nuevo usuario con rol CLIENTE por defecto
    Usuario nuevoUsuario = new Usuario();
    nuevoUsuario.setNombre(nombre);
    nuevoUsuario.setUsuario(usuario);
    nuevoUsuario.setEmail(email);
    nuevoUsuario.setContrasena(contrasena);
    nuevoUsuario.setRol("CLIENTE");
    nuevoUsuario.setTelefono("");
    nuevoUsuario.setFotoPerfil(null);

    // Guardar el usuario en el repositorio
    usuarioService.save(nuevoUsuario);

    // Redirigir al login con mensaje de confirmación
    flash.addFlashAttribute("mensaje", "Cuenta creada exitosamente. Por favor inicia sesión.");
    return "redirect:/login-page";
    }
}
