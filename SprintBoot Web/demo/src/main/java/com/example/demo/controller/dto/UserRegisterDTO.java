package com.example.demo.controller.dto;

import com.example.demo.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;
    
    @Size(max = 100, message = "El apellido no debe exceder 100 caracteres")
    private String apellido;
    
    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    private String usuario;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 255, message = "La contraseña debe tener entre 6 y 255 caracteres")
    private String contrasena;
    
    @NotNull(message = "La confirmación de contraseña es obligatoria")
    private String contrasenaConfirm;
    
    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    private String telefono;
    
    @Size(max = 20, message = "La cédula no debe exceder 20 caracteres")
    private String cedula;
    
    @NotNull(message = "El rol es obligatorio")
    private UserRole rol;
}
