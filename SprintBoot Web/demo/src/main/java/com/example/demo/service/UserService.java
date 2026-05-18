package com.example.demo.service;

import com.example.demo.controller.dto.*;
import com.example.demo.entities.UserEntity;
import com.example.demo.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {
    // Registro
    UserEntity register(UserRegisterDTO dto);
    
    // Login
    Optional<UserEntity> login(UserLoginDTO dto);
    
    // CRUD
    UserProfileDTO findById(Long id);
    List<UserResponseDTO> findAll();
    List<UserResponseDTO> findByRol(UserRole rol);
    UserEntity updateProfile(Long id, UserUpdateDTO dto);
    void deleteUser(Long id);
    
    // Utilidades
    boolean existsByUsuario(String usuario);
    boolean existsByEmail(String email);
    boolean existsByCedula(String cedula);
}
