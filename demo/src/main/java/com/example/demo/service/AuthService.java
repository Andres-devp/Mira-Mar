package com.example.demo.service;

import com.example.demo.entities.Cliente;

public interface AuthService {

    Cliente autenticar(String username, String password);

    Cliente registrar(String nombre, String usuario, String email,
                      String contrasena, String contrasenaConfirm);
}
