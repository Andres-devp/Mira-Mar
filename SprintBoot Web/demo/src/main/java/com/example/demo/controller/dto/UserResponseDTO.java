package com.example.demo.controller.dto;

import com.example.demo.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    
    private Long id;
    private String usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String fotoPerfil;
    private UserRole rol;
    private Boolean activo;
}
