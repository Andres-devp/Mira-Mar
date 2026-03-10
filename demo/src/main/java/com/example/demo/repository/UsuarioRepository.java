package com.example.demo.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.Usuario;

@Repository
public class UsuarioRepository {
    private final Map<Integer, Usuario> usuarios = new HashMap<>();

    public UsuarioRepository() {
        
        usuarios.put(1, new Usuario(1, "Administrador", "admin", "admin123", "ADMIN", "admin@miramar.com", "0000000000", null));
        
        usuarios.put(2, new Usuario(2, "Andres Doncel", "andres", "password123456", "ADMIN", "andres@gmail.com", "1234567890", null));
        usuarios.put(3, new Usuario(3, "Ohcar", "ohca", "password1234", "CLIENTE", "ohcar@gmail.com", "9876543210", null));
        usuarios.put(4, new Usuario(4, "Maria", "mari", "password1234", "CLIENTE", "maria@gmail.com", "9876543210", null));   
        usuarios.put(5, new Usuario(5, "admin", "admin", "123", "ADMIN", "admin@gmail.com", "9876543210", null));   
        usuarios.put(6, new Usuario(6, "Nicolas", "nico", "123", "CLIENTE", "nico@gmail.com", "9876543210", null));   
    }

    public Usuario findById(int id) {
        return usuarios.get(id);
    }

    /**
     * Busca un usuario por su nombre de usuario (campo "usuario").
     * @param username valor del campo usuario
     * @return usuario encontrado o null si no existe
     */
    public Usuario findByUsername(String username) {
        return usuarios.values().stream()
                .filter(u -> u.getUsuario().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Collection<Usuario> findAll() {
        return usuarios.values();
    }

    public Usuario save(Usuario usuario) {
        // if id is zero (default for primitive int), assign a new one (simple auto-increment)
        if (usuario.getId() == 0) {
            int maxId = usuarios.keySet().stream().max(Integer::compareTo).orElse(0);
            usuario.setId(maxId + 1);
        }
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    public void deleteById(int id) {
        usuarios.remove(id);
    }

    public Usuario findByEmail(String email) {
    return usuarios.values().stream()
            .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElse(null);
    }


}
