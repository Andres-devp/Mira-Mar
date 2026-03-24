package com.example.demo.service;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Client;
import com.example.demo.entities.Operator;
import com.example.demo.exception.RegistrationException;
import com.example.demo.repository.AdministratorRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private ClientRepository clienteRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @Override
    @Transactional(readOnly = true)
    public AuthenticatedUser autenticar(String username, String password) {
        Administrator administrador = administratorRepository.findByUsuario(username)
                .filter(a -> a.getContrasena().equals(password))
                .orElse(null);
        if (administrador != null) {
            return new AuthenticatedUser(administrador.getId(), "ADMIN");
        }

        Operator operador = operatorRepository.findByUsuario(username)
            .or(() -> operatorRepository.findByEmail(username))
            .or(() -> operatorRepository.findByCedula(username))
            .filter(o -> o.getContrasena().equals(password))
            .orElse(null);
        if (operador != null) {
            return new AuthenticatedUser(operador.getId(), "OPERATOR");
        }

        Client cliente = clienteRepository.findByUsuario(username)
                .filter(c -> c.getContrasena().equals(password))
                .orElse(null);
        if (cliente != null) {
            return new AuthenticatedUser(cliente.getId(), "CLIENT");
        }

        return null;
    }

    @Override
    public Client registrar(String nombre, String usuario, String email,
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

        Client nuevo = new Client();
        nuevo.setNombre(nombre);
        nuevo.setUsuario(usuario);
        nuevo.setEmail(email);
        nuevo.setContrasena(contrasena);
        nuevo.setTelefono("");
        return clienteRepository.save(nuevo);
    }
}
