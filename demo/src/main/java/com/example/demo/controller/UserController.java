package com.example.demo.controller;

import com.example.demo.entities.Client;
import com.example.demo.service.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private ClientService clienteService;

    @GetMapping("")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", clienteService.getAllClientes());
        return "Usuarios/usuarios-tabla";
    }

    @GetMapping("/{id}")
    public String usuarioPorId(Model model, @PathVariable Long id, HttpSession session) {
        Client cliente = clienteService.getClienteById(id);
        Long usuarioIdSesion = (Long) session.getAttribute("usuarioId");
        String usuarioRolSesion = (String) session.getAttribute("usuarioRol");

        boolean perfilPropio = usuarioIdSesion != null && usuarioIdSesion.equals(id);
        boolean admin = "ADMIN".equals(usuarioRolSesion);

        model.addAttribute("usuario", cliente);
        model.addAttribute("mostrarEliminarCuenta", perfilPropio);
        model.addAttribute("mostrarEditarPerfil", perfilPropio || admin);
        return "Usuarios/usuario-detail";
    }

    @GetMapping("/add")
    public String crearFormulario(Model model) {
        model.addAttribute("clienteForm", new Client());
        return "Usuarios/usuario-form";
    }

    @PostMapping("/add")
    public String guardarUsuario(@ModelAttribute("clienteForm") Client cliente) {
        clienteService.saveCliente(cliente);
        return "redirect:/usuarios";
    }

    @GetMapping("/edit/{id}")
    public String editarFormulario(Model model, @PathVariable Long id) {
        Client cliente = clienteService.getClienteById(id);
        model.addAttribute("clienteForm", cliente);
        return "Usuarios/usuario-form";
    }

    @PostMapping("/edit/{id}")
    public String actualizarUsuario(@ModelAttribute("clienteForm") Client cliente, @PathVariable Long id) {
        cliente.setId(id);
        cliente.setRol("CLIENTE");
        clienteService.saveCliente(cliente);
        return "redirect:/usuarios/" + id;
    }

    @PostMapping("/delete/{id}")
    public String eliminarUsuario(@PathVariable Long id, HttpSession session) {
        String usuarioRolSesion = (String) session.getAttribute("usuarioRol");
        if (!"ADMIN".equals(usuarioRolSesion)) {
            return "redirect:/login-page";
        }

        clienteService.deleteCliente(id);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/delete-account")
    public String eliminarCuentaPropia(HttpSession session) {
        Long usuarioIdSesion = (Long) session.getAttribute("usuarioId");
        if (usuarioIdSesion == null) {
            return "redirect:/login-page";
        }

        clienteService.deleteCliente(usuarioIdSesion);
        session.invalidate();
        return "redirect:/";
    }
}
