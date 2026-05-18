package com.example.demo.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {
    
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;
    
    @Size(max = 100, message = "El apellido no debe exceder 100 caracteres")
    private String apellido;
    
    @Email(message = "El email debe ser válido")
    private String email;
    
    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    private String telefono;
    
    @Size(max = 255, message = "La URL de foto no debe exceder 255 caracteres")
    private String fotoPerfil;
    
    @Size(min = 6, max = 255, message = "La contraseña debe tener entre 6 y 255 caracteres")
    private String contrasena;
    
    private String contrasenaConfirm;
}
