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
    public String Usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.searchAll());
        return "Usuarios/usuarios-tabla";
    }

    @GetMapping("/{id}")
    public String UsuarioPorId(Model model, @PathVariable ("id") Integer id) {

        Usuario usuario = usuarioService.searchById(id);
        model.addAttribute("usuario", usuario);
        return "Usuarios/usuario-detail";
    }

    // --- new CRUD mappings ---

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
        // ensure the id is set in case the form didn't include it
        usuario.setId(id);
        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }

    @PostMapping("/delete/{id}")
    public String eliminarUsuario(@PathVariable("id") Integer id) {
        usuarioService.deleteById(id);
        return "redirect:/usuarios";
    }
}

