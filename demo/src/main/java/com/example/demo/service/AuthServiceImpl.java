package com.example.demo.service;

import com.example.demo.entities.Cliente;
import com.example.demo.exception.RegistrationException;
import com.example.demo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public Cliente autenticar(String username, String password) {
        return clienteRepository.findByUsuario(username)
            .filter(c -> c.getContrasena().equals(password))
            .orElse(null);
    }

    @Override
    public Cliente registrar(String nombre, String usuario, String email,
                             String contrasena, String contrasenaConfirm) {
        nombre = nombre != null ? nombre.trim() : "";
        usuario = usuario != null ? usuario.trim() : "";
        email = email != null ? email.trim() : "";

        if (nombre.isEmpty() || usuario.isEmpty() || email.isEmpty()
                || contrasena == null || contrasena.isEmpty()
                || contrasenaConfirm == null || contrasenaConfirm.isEmpty()) {
            throw new RegistrationException("Todos los campos son obligatorios");
        }

        if (!contrasena.equals(contrasenaConfirm)) {
            throw new RegistrationException("Las contraseñas no coinciden");
        }

        if (clienteRepository.findByUsuario(usuario).isPresent()) {
            throw new RegistrationException("El nombre de usuario ya está en uso");
        }

        if (clienteRepository.findByEmail(email).isPresent()) {
            throw new RegistrationException("El correo electrónico ya está registrado");
        }

        Cliente nuevo = new Cliente();
        nuevo.setNombre(nombre);
        nuevo.setUsuario(usuario);
        nuevo.setEmail(email);
        nuevo.setContrasena(contrasena);
        nuevo.setRol("CLIENTE");
        nuevo.setTelefono("");
        return clienteRepository.save(nuevo);
    }
}
