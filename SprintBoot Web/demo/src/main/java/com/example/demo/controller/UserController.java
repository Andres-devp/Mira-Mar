package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;

import com.example.demo.controller.dto.*;
import com.example.demo.entities.Client;
import com.example.demo.entities.UserEntity;
import com.example.demo.enums.UserRole;
import com.example.demo.service.ClientService;
import com.example.demo.service.UserService;

import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Usuarios", description = "Gestión unificada de usuarios (Admin, Operador, Cliente)")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserProfileDTO> register(@Valid @RequestBody UserRegisterDTO dto) {
        try {
            UserEntity user = userService.register(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                userService.findById(user.getId())
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuario")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserProfileDTO> login(@Valid @RequestBody UserLoginDTO dto) {
        var user = userService.login(dto);
        if (user.isPresent()) {
            return ResponseEntity.ok(userService.findById(user.get().getId()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping
    @Operation(summary = "Listar clientes activos")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<Client>> getAllUsers() {
        List<Client> activos = clientService.getAllClientes().stream()
            .filter(c -> Boolean.TRUE.equals(c.getActivo()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(activos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener perfil de cliente por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'OPERATOR')")
    public ResponseEntity<Client> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClienteById(id));
    }

    @PostMapping("/add")
    @Operation(summary = "Crear cliente")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Client> createUser(@RequestBody Client cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.saveCliente(cliente));
    }

    @GetMapping("/rol/{rol}")
    @Operation(summary = "Listar usuarios por rol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRol(@PathVariable UserRole rol) {
        return ResponseEntity.ok(userService.findByRol(rol));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar perfil de cliente")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR') or #id == authentication.principal")
    public ResponseEntity<Client> updateUser(
            @PathVariable Long id,
            @RequestBody Client datos) {
        return ResponseEntity.ok(clientService.updateCliente(id, datos));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cuenta de cliente (bloquea si hay reservas/servicios activos)")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR') or #id == authentication.principal")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        clientService.eliminarCuenta(id);
        return ResponseEntity.noContent().build();
    }
}
