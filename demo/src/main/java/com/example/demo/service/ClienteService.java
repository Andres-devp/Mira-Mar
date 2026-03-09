package com.example.demo.service;

import com.example.demo.entities.Cliente;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    @Transactional(readOnly = true)
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Cliente getClienteById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + id, id));
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> findClienteById(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Cliente saveCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    @Transactional
    public void deleteCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + id, id));
        clienteRepository.delete(cliente);
    }
}
