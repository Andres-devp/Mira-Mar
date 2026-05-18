package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.demo.controller.dto.*;
import com.example.demo.entities.UserEntity;
import com.example.demo.enums.UserRole;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Usuarios", description = "Gestión unificada de usuarios (Admin, Operador, Cliente)")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario")
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
    public ResponseEntity<UserProfileDTO> login(@Valid @RequestBody UserLoginDTO dto) {
        var user = userService.login(dto);
        if (user.isPresent()) {
            return ResponseEntity.ok(userService.findById(user.get().getId()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener perfil de usuario por ID")
    public ResponseEntity<UserProfileDTO> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rol/{rol}")
    @Operation(summary = "Listar usuarios por rol")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRol(@PathVariable UserRole rol) {
        return ResponseEntity.ok(userService.findByRol(rol));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar perfil de usuario")
    public ResponseEntity<UserProfileDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO dto) {
        try {
            userService.updateProfile(id, dto);
            return ResponseEntity.ok(userService.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar usuario (soft delete)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
