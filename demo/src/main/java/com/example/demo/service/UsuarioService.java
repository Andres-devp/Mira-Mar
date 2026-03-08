package com.example.demo.service;

import java.util.Collection;

import com.example.demo.entities.Usuario;

public interface UsuarioService {
    public Usuario searchById(int id);
    public Collection<Usuario> searchAll();

    // obtiene un usuario por nombre de usuario (login)
    public Usuario searchByUsername(String username);

    // obtiene un usuario por el correo
    public Usuario searchByEmail(String email);

    // create or update a user
    public Usuario save(Usuario usuario);
    
    // delete by id
    public void deleteById(int id);


}
