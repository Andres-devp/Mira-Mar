package com.example.demo.controller;

import com.example.demo.service.ClientService;
import com.example.demo.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/operator")
public class OperatorController {

    @Autowired
    private ReservationService reservaService;

    @Autowired
    private ClientService clienteService;

    @GetMapping({"", "/"})
    public String operator() {
        return "operator";
    }

    @GetMapping("/reservas")
    public String reservasTabla(Model model) {
        model.addAttribute("reservas", reservaService.getAllReservas());
        model.addAttribute("panelHome", "/operator");
        return "reservations/reservations-table";
    }

    @GetMapping("/clientes")
    public String clientesTabla(Model model) {
        model.addAttribute("usuarios", clienteService.getAllClientes());
        model.addAttribute("panelHome", "/operator");
        model.addAttribute("mostrarCrearUsuario", false);
        return "Usuarios/usuarios-tabla";
    }
}
