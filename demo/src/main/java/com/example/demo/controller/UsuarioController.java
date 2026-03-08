package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entities.Usuario;
import com.example.demo.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("")
    public String Usuarios(Model model, jakarta.servlet.http.HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/login-page";
        }

        boolean esAdminOOperador = "ADMIN".equalsIgnoreCase(usuarioLogueado.getRol())
                || "OPERADOR".equalsIgnoreCase(usuarioLogueado.getRol());

        if (!esAdminOOperador) {
            return "redirect:/perfil";
        }

        model.addAttribute("usuarios", usuarioService.searchAll());
        return "Usuarios/usuarios-tabla";
    }

    @GetMapping("/{id}")
    public String UsuarioPorId(Model model,
                               @PathVariable("id") Integer id,
                               jakarta.servlet.http.HttpSession session) {

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/login-page";
        }

        boolean esAdminOOperador = "ADMIN".equalsIgnoreCase(usuarioLogueado.getRol())
                || "OPERADOR".equalsIgnoreCase(usuarioLogueado.getRol());

        if (!esAdminOOperador && usuarioLogueado.getId() != id) {
            return "redirect:/perfil";
        }

        Usuario usuario = usuarioService.searchById(id);
        model.addAttribute("usuario", usuario);
        return "Usuarios/usuario-detail";
    }

    @PostMapping("/delete-account/{id}")
    public String eliminarCuenta(@PathVariable("id") Integer id,
                                 jakarta.servlet.http.HttpSession session) {

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/login-page";
        }

        if (usuarioLogueado.getId() != id) {
            return "redirect:/perfil";
        }

        usuarioService.deleteById(id);
        session.invalidate();

        return "redirect:/login-page";
    }

    @PostMapping("/delete/{id}")
    public String eliminarUsuario(@PathVariable("id") Integer id,
                                  jakarta.servlet.http.HttpSession session) {

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/login-page";
        }

        boolean esAdmin = "ADMIN".equalsIgnoreCase(usuarioLogueado.getRol());

        if (!esAdmin) {
            return "redirect:/perfil";
        }

        usuarioService.deleteById(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/add")
    public String crearFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "Usuarios/usuario-form";
    }

    @PostMapping("/add")
    public String guardarUsuario(Usuario usuario) {
        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/edit/{id}")
    public String editarFormulario(Model model, @PathVariable("id") Integer id) {
        Usuario usuario = usuarioService.searchById(id);
        model.addAttribute("usuario", usuario);
        return "Usuarios/usuario-form";
    }

    @PostMapping("/edit/{id}")
    public String actualizarUsuario(Usuario usuario, @PathVariable("id") Integer id) {
        usuario.setId(id);
        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }
}