package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private int  id;
    private String nombre;
    private String usuario;
    private String contrasena;
    private String rol; // "ADMIN" o "CLIENTE"
    private String email;
    private String telefono;
    private String fotoPerfil;
}
