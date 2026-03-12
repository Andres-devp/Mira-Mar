package com.example.demo.service;

import com.example.demo.entities.Cliente;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente getClienteById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + id, id));
    }

    @Override
    public Cliente saveCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void deleteCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + id, id));
        clienteRepository.delete(cliente);
    }
}
