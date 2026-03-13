package com.example.demo.service;

import com.example.demo.entities.Client;

public interface AuthService {

    Client autenticar(String username, String password);

    Client registrar(String nombre, String usuario, String email,
                      String contrasena, String contrasenaConfirm);
}
