package com.example.demo.controller;

import com.example.demo.entities.Cliente;
import com.example.demo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", clienteService.getAllClientes());
        return "Usuarios/usuarios-tabla";
    }

    @GetMapping("/{id}")
    public String usuarioPorId(Model model, @PathVariable Long id) {
        Cliente cliente = clienteService.getClienteById(id);
        model.addAttribute("usuario", cliente);
        return "Usuarios/usuario-detail";
    }

    @GetMapping("/add")
    public String crearFormulario(Model model) {
        model.addAttribute("usuario", new Cliente());
        return "Usuarios/usuario-form";
    }

    @PostMapping("/add")
    public String guardarUsuario(@ModelAttribute("usuario") Cliente cliente) {
        clienteService.saveCliente(cliente);
        return "redirect:/usuarios";
    }

    @GetMapping("/edit/{id}")
    public String editarFormulario(Model model, @PathVariable Long id) {
        Cliente cliente = clienteService.getClienteById(id);
        model.addAttribute("usuario", cliente);
        return "Usuarios/usuario-form";
    }

    @PostMapping("/edit/{id}")
    public String actualizarUsuario(@ModelAttribute("usuario") Cliente cliente, @PathVariable Long id) {
        cliente.setId(id);
        clienteService.saveCliente(cliente);
        return "redirect:/usuarios";
    }

    @PostMapping("/delete/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        clienteService.deleteCliente(id);
        return "redirect:/usuarios";
    }
}

