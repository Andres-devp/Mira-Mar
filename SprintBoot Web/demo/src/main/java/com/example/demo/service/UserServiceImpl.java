package com.example.demo.service;

import com.example.demo.controller.dto.*;
import com.example.demo.entities.UserEntity;
import com.example.demo.enums.UserRole;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserEntity register(UserRegisterDTO dto) {
        if (existsByUsuario(dto.getUsuario())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        if (existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (dto.getCedula() != null && existsByCedula(dto.getCedula())) {
            throw new IllegalArgumentException("La cédula ya está registrada");
        }
        
        if (!dto.getContrasena().equals(dto.getContrasenaConfirm())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        
        UserEntity user = UserEntity.builder()
                .usuario(dto.getUsuario())
                .email(dto.getEmail())
                .contrasena(dto.getContrasena())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .cedula(dto.getCedula())
                .telefono(dto.getTelefono())
                .rol(dto.getRol())
                .activo(true)
                .build();
        
        return userRepository.save(user);
    }
    
    @Override
    public Optional<UserEntity> login(UserLoginDTO dto) {
        String username = dto.getUsername();
        Optional<UserEntity> user = userRepository.findByUsuario(username)
                .or(() -> userRepository.findByEmail(username))
                .or(() -> userRepository.findByCedula(username));
        
        if (user.isPresent() && user.get().getContrasena().equals(dto.getPassword())) {
            UserEntity entity = user.get();
            entity.setUltimaConexion(LocalDateTime.now());
            return Optional.of(userRepository.save(entity));
        }
        
        return Optional.empty();
    }
    
    @Override
    public UserProfileDTO findById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return toProfileDTO(user);
    }
    
    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserResponseDTO> findByRol(UserRole rol) {
        return userRepository.findByRol(rol).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public UserEntity updateProfile(Long id, UserUpdateDTO dto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        if (dto.getNombre() != null) user.setNombre(dto.getNombre());
        if (dto.getApellido() != null) user.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) user.setTelefono(dto.getTelefono());
        if (dto.getFotoPerfil() != null) user.setFotoPerfil(dto.getFotoPerfil());
        
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("El email ya está en uso");
            }
            user.setEmail(dto.getEmail());
        }
        
        if (dto.getContrasena() != null) {
            if (!dto.getContrasena().equals(dto.getContrasenaConfirm())) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }
            user.setContrasena(dto.getContrasena());
        }
        
        return userRepository.save(user);
    }
    
    @Override
    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        user.setActivo(false);
        userRepository.save(user);
    }
    
    @Override
    public boolean existsByUsuario(String usuario) {
        return userRepository.existsByUsuario(usuario);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public boolean existsByCedula(String cedula) {
        return userRepository.existsByCedula(cedula);
    }
    
    private UserResponseDTO toResponseDTO(UserEntity user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .usuario(user.getUsuario())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .email(user.getEmail())
                .telefono(user.getTelefono())
                .fotoPerfil(user.getFotoPerfil())
                .rol(user.getRol())
                .activo(user.getActivo())
                .build();
    }
    
    private UserProfileDTO toProfileDTO(UserEntity user) {
        return UserProfileDTO.builder()
                .id(user.getId())
                .usuario(user.getUsuario())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .email(user.getEmail())
                .cedula(user.getCedula())
                .telefono(user.getTelefono())
                .fotoPerfil(user.getFotoPerfil())
                .rol(user.getRol())
                .fechaRegistro(user.getFechaRegistro())
                .ultimaConexion(user.getUltimaConexion())
                .activo(user.getActivo())
                .build();
    }
}
