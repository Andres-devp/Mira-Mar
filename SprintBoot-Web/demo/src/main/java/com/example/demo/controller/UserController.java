package com.example.demo.controller;

import com.example.demo.entities.Client;
import com.example.demo.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Usuarios", description = "Gestión de usuarios (clientes)")
public class UserController {

    @Autowired
    private ClientService clienteService;

    @GetMapping({"/all", ""})
    @Operation(summary = "Listar todos los clientes")
    public List<Client> usuarios() {
        return clienteService.getAllClientes();
    }

    @GetMapping("/find/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public Client findById(@PathVariable Long id) {
        return clienteService.getClienteById(id);
    }

    @PostMapping("/add")
    @Operation(summary = "Crear nuevo cliente")
    public Client createUser(@RequestBody Client cliente) {
        return clienteService.saveCliente(cliente);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualizar cliente existente")
    public Client updateUser(@PathVariable Long id, @RequestBody Client cliente) {
        cliente.setId(id);
        return clienteService.saveCliente(cliente);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar cliente por ID")
    public void deleteUser(@PathVariable Long id) {
        clienteService.deleteCliente(id);
    }
}
