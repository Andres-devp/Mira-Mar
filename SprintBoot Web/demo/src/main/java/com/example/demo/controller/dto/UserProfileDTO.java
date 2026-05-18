package com.example.demo.controller.dto;

import com.example.demo.enums.UserRole;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    
    private Long id;
    private String usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String fotoPerfil;
    private String cedula;
    private UserRole rol;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimaConexion;
    private Boolean activo;
}
