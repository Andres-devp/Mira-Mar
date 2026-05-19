package com.example.demo.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientProfileDTO {
    
    private Long id;
    private String nombre;
    private String usuario;
    private String email;
    private String telefono;
    private String fotoPerfil;
}
