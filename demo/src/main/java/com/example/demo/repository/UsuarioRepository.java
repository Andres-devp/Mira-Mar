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
        // 10 usuarios de ejemplo
        usuarios.put(1, new Usuario(1, "Andres Doncel", "andres", "password123456", "admin", "andres@gmail.com", "1234567890"));
        usuarios.put(2, new Usuario(2, "Ohcar", "ohca", "password1234", "cliente", "ohcar@gmail.com", "9876543210"));
        usuarios.put(3, new Usuario(3, "Maria", "mari", "password1234", "cliente", "maria@gmail.com", "9876543210"));   
    }

    public Usuario findById(int id) {
        return usuarios.get(id);    
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


}
