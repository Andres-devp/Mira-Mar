package com.example.demo.repository;

import com.example.demo.entities.Cliente;
import com.example.demo.entities.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepository {

    private final ClienteRepository clienteRepository;

    public UsuarioRepository(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    private Usuario toUsuario(Cliente c) {
        return new Usuario(
                c.getId().intValue(),
                c.getNombre(),
                c.getUsuario(),
                c.getContrasena(),
                c.getRol(),
                c.getEmail(),
                c.getTelefono(),
                c.getFotoPerfil()
        );
    }

    private Cliente toCliente(Usuario u) {
        Cliente c = new Cliente();
        if (u.getId() != 0) {
            c.setId((long) u.getId());
        }
        c.setNombre(u.getNombre());
        c.setUsuario(u.getUsuario());
        c.setContrasena(u.getContrasena());
        c.setRol(u.getRol());
        c.setEmail(u.getEmail());
        c.setTelefono(u.getTelefono());
        c.setFotoPerfil(u.getFotoPerfil());
        return c;
    }

    public Usuario findById(int id) {
        return clienteRepository.findById((long) id)
                .map(this::toUsuario)
                .orElse(null);
    }

    public Usuario findByUsername(String username) {
        return clienteRepository.findByUsuario(username)
                .map(this::toUsuario)
                .orElse(null);
    }

    public Collection<Usuario> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::toUsuario)
                .collect(Collectors.toList());
    }

    public Usuario save(Usuario usuario) {
        Cliente saved = clienteRepository.save(toCliente(usuario));
        return toUsuario(saved);
    }

    public void deleteById(int id) {
        clienteRepository.deleteById((long) id);
    }

    public Usuario findByEmail(String email) {
        return clienteRepository.findByEmail(email)
                .map(this::toUsuario)
                .orElse(null);
    }
}
