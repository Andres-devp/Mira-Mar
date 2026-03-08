package com.example.demo.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Usuario;
import com.example.demo.repository.UsuarioRepository;
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public Usuario searchById(int id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Collection<Usuario> searchAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario searchByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteById(int id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario searchByEmail(String email) {
    return usuarioRepository.findByEmail(email);
    }
    
}
