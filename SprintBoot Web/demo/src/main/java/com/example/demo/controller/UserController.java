package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Client;
import com.example.demo.entities.Reservation;
import com.example.demo.service.ClientService;
import com.example.demo.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Usuarios", description = "Gestión de usuarios (clientes)")
public class UserController {

    @Autowired
    private ClientService clienteService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping({"/all", ""})
    @Operation(summary = "Listar todos los clientes")
    public List<Client> usuarios() {
        return clienteService.getAllClientes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public Client findById(@PathVariable Long id) {
        return clienteService.getClienteById(id);
    }

    @PostMapping("/add")
    @Operation(summary = "Crear nuevo cliente")
    public Client createUser(@RequestBody Client cliente) {
        return clienteService.saveCliente(cliente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente existente")
    public Client updateUser(@PathVariable Long id, @RequestBody Client cliente) {
        cliente.setId(id);
        return clienteService.saveCliente(cliente);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente por ID")
    public void deleteUser(@PathVariable Long id) {
        clienteService.deleteCliente(id);
    }

    @GetMapping("/{id}/reservations")
    @Operation(summary = "Obtener historial de reservas de un cliente")
    public List<Reservation> getReservaciones(@PathVariable Long id) {
        return reservationService.getReservacionesByCliente(id);
    }
}
