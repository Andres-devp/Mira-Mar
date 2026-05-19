package com.example.demo.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperatorResponseDTO {
    
    private Long id;
    private String nombre;
    private String apellido;
    private String usuario;
    private String email;
    private String cedula; 
    private String telefono;
}
